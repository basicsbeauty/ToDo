package com.enlaps.m.and.todo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class ActivityHome extends AppCompatActivity {

    private ListView m_lvTodo;
    private EditText m_etInput;

    private ArrayList<String> m_arrayList;
    private ArrayAdapter<String> m_adapter;

    static final String FILE_TODO = "todo.txt";

    // State
        private int currentItem;

    // Intents
        static final String INTENT_MESSAGE_ITEM_TEXT = "INTENT_MESSAGE_ITEM_TEXT";
        static final int INTENT_REQUEST_CODE = 0;

    // Database
        private SqliteHelper    m_dbHelper;
        private SQLiteDatabase  m_db;

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


        // ItemList: Load
            loadItemsFromDB();

        // ListView: Adapter

            m_adapter = new ArrayAdapter<String>( this, R.layout.simple_list_item_1_text_black, m_arrayList);

            m_lvTodo.setAdapter(m_adapter);

        // ListView: Item: Long Click
            listViewItemClickSetup();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    public void handlerBtnAddItemOnClick(View view) {

        String item = m_etInput.getText().toString();

        // EditText: New Item:
            m_arrayList.add(item);
            m_adapter.notifyDataSetChanged();

        // EditText: Clear
        m_etInput.setText("");

        // Add Item to Table
            insertRow(item);
    }

    protected long insertRow(String itemName) {

        ContentValues rowValues = new ContentValues();

        rowValues.put(DBSchema.ItemTable.COL_NAME_TITLE, itemName);

        return m_db.insert(DBSchema.ItemTable.TABLE_NAME, null, rowValues);
    }

    protected int deleteRow(String itemName) {

        String selection        = DBSchema.ItemTable.COL_NAME_TITLE + " LIKE ?";
        String selectionArgs[]  = {itemName};

        return m_db.delete(DBSchema.ItemTable.TABLE_NAME, selection, selectionArgs);
    }

    protected int updateRow(String itemNameOld, String itemNameNew) {

        String  selection       = DBSchema.ItemTable.COL_NAME_TITLE + " LIKE ?";
        String  selectionArgs[] = {itemNameOld};
        ContentValues   rowValues = new ContentValues();

        rowValues.put(DBSchema.ItemTable.COL_NAME_TITLE, itemNameNew);

        return m_db.update(DBSchema.ItemTable.TABLE_NAME, rowValues, selection, selectionArgs);
    }

    private void listViewItemClickSetup() {

        m_lvTodo.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                        Intent i = new Intent(ActivityHome.this, ActivityItemEdit.class);

                        ActivityHome.this.currentItem = pos;
                        i.putExtra(ActivityHome.INTENT_MESSAGE_ITEM_TEXT, m_arrayList.get(pos));

                        startActivityForResult( i, ActivityHome.INTENT_REQUEST_CODE);
                    }
                }
        );

        m_lvTodo.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                        ActivityHome.this.deleteRow(m_arrayList.get(i));

                        m_arrayList.remove(i);
                        m_adapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );

    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data) {

        if( (requestCode == ActivityHome.INTENT_REQUEST_CODE)
         && (resultCode == RESULT_OK)
         && (data != null)) {

            String oldValue = m_arrayList.get(ActivityHome.this.currentItem);
            String newValue = data.getStringExtra(ActivityItemEdit.INTENT_MESSAGE_ITEM_VALUE_NEW);

            // Update database
                updateRow( oldValue, newValue);

            m_arrayList.set( ActivityHome.this.currentItem, newValue);
            m_adapter.notifyDataSetChanged();
        }
    }

    private void loadItemsFromDB() {


        String      selection = null;
        String[]    selectionArgs = {};
        String[]    projection = {DBSchema.ItemTable.COL_NAME_TITLE};

        Cursor      c = m_db.query(DBSchema.ItemTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if( c == null) {
            return;
        }

        int itemTitleIndex = c.getColumnIndexOrThrow(DBSchema.ItemTable.COL_NAME_TITLE);

        if( c.moveToFirst()) {

            do {
                m_arrayList.add(c.getString(itemTitleIndex));
            }while ( c.moveToNext());
        }
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
