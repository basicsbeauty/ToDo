package com.enlaps.m.and.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ActivityItemEdit extends AppCompatActivity {

    // Components
        EditText m_etItemNewValue;

    static final String INTENT_MESSAGE_ITEM_VALUE_NEW = "INTENT_MESSAGE_ITEM_TEXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        // Components
            m_etItemNewValue = (EditText) findViewById(R.id.etItemNewValue);

        try {


            Intent i = getIntent();

            String itemTextOld = i.getStringExtra( ActivityHome.INTENT_MESSAGE_ITEM_TEXT);

            m_etItemNewValue.setText(itemTextOld);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSaveOnClick(View view) {
        Intent rIntent = new Intent();
        rIntent.putExtra( ActivityItemEdit.INTENT_MESSAGE_ITEM_VALUE_NEW, m_etItemNewValue.getText().toString());
        setResult( RESULT_OK, rIntent);
        finish();
    }
}
