package com.enlaps.m.and.todo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class ActivityHome extends AppCompatActivity {

    private ListView m_lvTodo;
    private EditText m_etInput;

    private ArrayList<String> m_arrayList;
    private ArrayAdapter<String> m_adapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        m_lvTodo = (ListView) findViewById(R.id.lvTodo);
        m_etInput = (EditText) findViewById(R.id.etInput);

        m_arrayList = new ArrayList<String>();

        m_adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_item_text_black, m_arrayList);

        m_lvTodo.setAdapter(m_adapter);
        /*
        m_lvTodo.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, m_arrayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                CheckedTextView checkedTxtView = (CheckedTextView) super.getView(position, convertView, parent);

                String yourValue = m_arrayList.get(position);
                checkedTxtView.setText(yourValue);
                checkedTxtView.setTextColor(this.getResources().getColor(android.R.color.black));
                // checkedTxtView.setTextColor(this.getResources().getColor(android.R.color.black));
                return textView;
            }

        });
        //*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void handlerBtnAddItemOnClick(View view) {

        String item = m_etInput.getText().toString();

        // EditText: New Item: Clear

        m_arrayList.add(m_etInput.getText().toString());

        m_adapter.notifyDataSetChanged();
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
