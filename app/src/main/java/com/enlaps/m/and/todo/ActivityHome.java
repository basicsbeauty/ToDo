package com.enlaps.m.and.todo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ActivityHome extends AppCompatActivity {

    private ListView m_lvTodo;
    private EditText m_etInput;

    private ArrayList<String> m_arrayList;
    private ArrayAdapter<String> m_adapter;

    private ItemAdapter   mItemAdapter;

    static final String FILE_TODO = "todo.txt";

    // State
        private int currentItem;

    // Intent(s)
        static final int INTENT_REQUEST_CODE_ADD  = 0;
        static final int INTENT_REQUEST_CODE_EDIT = 1;

        static final String INTENT_MESSAGE_REQUEST_CODE  = "INTENT_MESSAGE_REQUEST_CODE";
        static final String INTENT_MESSAGE_ITEM_TEXT     = "INTENT_MESSAGE_ITEM_TEXT";
        static final String INTENT_MESSAGE_ITEM_DUE_DATE = "INTENT_MESSAGE_ITEM_DUE_DATE";
        static final String INTENT_MESSAGE_ITEM_PRIORITY = "INTENT_MESSAGE_ITEM_PRIORITY";

    // Database
        private SqliteHelper    m_dbHelper;
        private SQLiteDatabase  m_db;

    private ArrayList<Item> mItemArray;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Components
            m_lvTodo = (ListView) findViewById(R.id.lvTodo);
            m_etInput = (EditText) findViewById(R.id.etInput);

        m_arrayList = new ArrayList<String>();

        // Database
            m_dbHelper = new SqliteHelper(this);
            m_db = m_dbHelper.getWritableDatabase();
            m_dbHelper.setDB(m_db);
            //m_dbHelper.onUpgrade( m_db, 1, 2);

        // ItemList: Load
            mItemArray = new ArrayList<Item>();
            loadItemsFromDB();

        // ListView: Adapter
            //m_adapter = new ArrayAdapter<String>( this, R.layout.simple_list_item_1_text_black, m_arrayList);
            //m_lvTodo.setAdapter(m_adapter);

            mItemAdapter = new ItemAdapter( this, mItemArray);
            m_lvTodo.setAdapter(mItemAdapter);

        // ListView: Item: Long Click
            listViewItemClickSetup();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    public void fabAddOnClick(View view) {
        Intent i = new Intent(ActivityHome.this, ActivityItemEdit.class);

        i.putExtra(ActivityHome.INTENT_MESSAGE_REQUEST_CODE, ActivityHome.INTENT_REQUEST_CODE_ADD);

        startActivityForResult(i, ActivityHome.INTENT_REQUEST_CODE_ADD);
    }

    public void handlerBtnAddItemOnClick(View view) {

        String item = m_etInput.getText().toString();

        // EditText: New Item:
            m_arrayList.add(item);
            m_adapter.notifyDataSetChanged();

        // EditText: Clear
        m_etInput.setText("");

        // Add Item to Table
            m_dbHelper.insertRow(item);
    }

    private void listViewItemClickSetup() {

        m_lvTodo.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                        Intent i = new Intent(ActivityHome.this, ActivityItemEdit.class);

                        ActivityHome.this.currentItem = pos;
                        Item itemTemp = mItemArray.get(pos);

                        i.putExtra( ActivityHome.INTENT_MESSAGE_REQUEST_CODE, ActivityHome.INTENT_REQUEST_CODE_EDIT);
                        i.putExtra( ActivityHome.INTENT_MESSAGE_ITEM_TEXT, itemTemp.title);
                        i.putExtra( ActivityHome.INTENT_MESSAGE_ITEM_DUE_DATE, itemTemp.dueDate.toString());
                        i.putExtra( ActivityHome.INTENT_MESSAGE_ITEM_PRIORITY, itemTemp.priority);

                        startActivityForResult(i, ActivityHome.INTENT_REQUEST_CODE_EDIT);
                    }
                }
        );

        m_lvTodo.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                        //ActivityHome.this.m_dbHelper.deleteRow(m_arrayList.get(i));

                        Item itemTemp = mItemArray.get(i);

                        ActivityHome.this.m_dbHelper.deleteRow( itemTemp.id);

                        mItemArray.remove(i);
                        mItemAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {

        if( (requestCode == ActivityHome.INTENT_REQUEST_CODE_ADD)
         && (resultCode == RESULT_OK)
         && (data != null)) {

            //String oldValue = m_arrayList.get(ActivityHome.this.currentItem);

            long dueDate = data.getLongExtra(ActivityItemEdit.INTENT_MESSAGE_ITEM_VALUE_DUE_DATE, System.currentTimeMillis());
            String title = data.getStringExtra(ActivityItemEdit.INTENT_MESSAGE_ITEM_VALUE_NEW);
            String priority = data.getStringExtra(ActivityItemEdit.INTENT_MESSAGE_ITEM_VALUE_PRIORITY);

            // Update database
                m_dbHelper.insertRow( title, new Date(dueDate), priority);
                //m_dbHelper.updateRow( oldValue, title);

            loadItemsFromDB();
            mItemAdapter.notifyDataSetChanged();
        }
    }

    private void loadItemsFromDB() {

        SimpleDateFormat dateFormatMMDDYY = new SimpleDateFormat("MM-dd-yyyy");

        String      selection = null;
        String[]    selectionArgs = {};
        String[]    projection = { DBSchema.ItemTable.COL_NAME_ID,
                                   DBSchema.ItemTable.COL_NAME_TITLE,
                                   DBSchema.ItemTable.COL_NAME_DUE_DATE,
                                   DBSchema.ItemTable.COL_NAME_PRIORITY};

        Cursor      c = m_db.query(DBSchema.ItemTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if( c == null) {
            return;
        }

        mItemArray.clear();

        int itemColIndexId = c.getColumnIndexOrThrow(DBSchema.ItemTable.COL_NAME_ID);
        int itemColIndexTitle = c.getColumnIndexOrThrow(DBSchema.ItemTable.COL_NAME_TITLE);
        int itemColIndexDueDate = c.getColumnIndexOrThrow(DBSchema.ItemTable.COL_NAME_DUE_DATE);
        int itemColIndexPriority = c.getColumnIndexOrThrow(DBSchema.ItemTable.COL_NAME_PRIORITY);

        if( c.moveToFirst()) {
            do {
                // m_arrayList.add(c.getString(itemColIndexTitle));

                Date dateTemp;

                try {
                    Log.d("Row: Date", c.getString(itemColIndexDueDate));
                    java.util.Date utilDate = dateFormatMMDDYY.parse(c.getString(itemColIndexDueDate));
                    dateTemp = new java.sql.Date(utilDate.getTime());
                }
                catch(Exception e) {
                    e.printStackTrace();
                    continue;
                }

                Item itemTemp = new Item( c.getInt(itemColIndexId),
                                          c.getString(itemColIndexTitle),
                                          dateTemp,
                                          c.getString(itemColIndexPriority));

                mItemArray.add(itemTemp);

            }while ( c.moveToNext());
        }

        Log.d("Count", Integer.toString(mItemArray.size()));
    }

    private void cleanUp() {
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityHome Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.enlaps.m.and.todo/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {

        cleanUp();

        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "ActivityHome Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.enlaps.m.and.todo/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

}
