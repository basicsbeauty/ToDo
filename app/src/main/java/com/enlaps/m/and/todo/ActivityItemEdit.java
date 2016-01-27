package com.enlaps.m.and.todo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class ActivityItemEdit extends AppCompatActivity {

    // Components
        Spinner     m_spnrPriority;
        EditText    m_etDueDate;
        EditText    m_etItemNewValue;

    enum ITEM_EDIT_STATE {
        ITEM_EDIT_STATE_DEFAULT,
        ITEM_EDIT_STATE_ADD,
        ITEM_EDIT_STATE_EDIT
    };

    protected ITEM_EDIT_STATE mItemEditState;

    static final String INTENT_MESSAGE_ITEM_VALUE_NEW = "INTENT_MESSAGE_ITEM_TEXT";
    static final String INTENT_MESSAGE_ITEM_VALUE_DUE_DATE = "INTENT_MESSAGE_ITEM_DUE_DATE";
    static final String INTENT_MESSAGE_ITEM_VALUE_PRIORITY = "INTENT_MESSAGE_ITEM_PRIORITY";

    // Intent
        Intent  mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        // Components
            getAllComponents();
            initComponents();

        try {
            mIntent = getIntent();

            // Mode
                int mode;
                mode = mIntent.getIntExtra( ActivityHome.INTENT_MESSAGE_REQUEST_CODE, ActivityHome.INTENT_REQUEST_CODE_ADD);
                if( ActivityHome.INTENT_REQUEST_CODE_ADD ==  mode) {
                    mItemEditState = ITEM_EDIT_STATE.ITEM_EDIT_STATE_ADD;
                    Log.d("Mode", "New");
                }
                else if( ActivityHome.INTENT_REQUEST_CODE_EDIT == mode) {
                    mItemEditState = ITEM_EDIT_STATE.ITEM_EDIT_STATE_EDIT;
                    Log.d("Mode", "Edit");
                }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Fill data
            if( ITEM_EDIT_STATE.ITEM_EDIT_STATE_EDIT == mItemEditState) {

                // Title
                    m_etItemNewValue.setText(mIntent.getStringExtra( ActivityHome.INTENT_MESSAGE_ITEM_TEXT));

                // Priority
                    String priority = mIntent.getStringExtra( ActivityHome.INTENT_MESSAGE_ITEM_PRIORITY);
                    if( "HIGH" == priority) {
                        m_spnrPriority.setSelection(0);
                    }
                    else if( "MEDIUM" == priority) {
                        m_spnrPriority.setSelection(1);
                    }
                    else {
                        m_spnrPriority.setSelection(2);
                    }

                // Date
                    m_etDueDate.setText( mIntent.getStringExtra(ActivityHome.INTENT_MESSAGE_ITEM_DUE_DATE));
            }
    }

    private void getAllComponents() {
        m_etDueDate = (EditText) findViewById(R.id.etDueDate);
        m_spnrPriority = (Spinner) findViewById(R.id.spnrPriority);
        m_etItemNewValue = (EditText) findViewById(R.id.etItemNewValue);
    }

    private void initComponents() {

        // Spinner
            ArrayAdapter<CharSequence> adapter;
            adapter = ArrayAdapter.createFromResource( this,
                                                       R.array.item_priority,
                                                       android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            m_spnrPriority.setAdapter(adapter);

        // Date

    }

    public void btnSaveOnClick(View view) {

        SimpleDateFormat dateFormatMMDDYY = new SimpleDateFormat("MM-dd-yyyy");

        // Item name: CAN'T be empty
            if( 0 == m_etItemNewValue.getText().toString().length()) {
                Toast.makeText( this, "Item can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }

        Intent rIntent = new Intent();

        // Fill date
            java.sql.Date dateTemp;

            try {
                Log.i("Date", m_etDueDate.getText().toString());
                java.util.Date utilDate = dateFormatMMDDYY.parse( m_etDueDate.getText().toString());
                dateTemp = new java.sql.Date(utilDate.getTime());
            }
            catch(Exception e) {
                e.printStackTrace();
                Toast.makeText( this, "Enter valid date", Toast.LENGTH_SHORT).show();
                return;
            }
            rIntent.putExtra( ActivityItemEdit.INTENT_MESSAGE_ITEM_VALUE_DUE_DATE, dateTemp.getTime());

            rIntent.putExtra( ActivityItemEdit.INTENT_MESSAGE_ITEM_VALUE_NEW, m_etItemNewValue.getText().toString());
            rIntent.putExtra( ActivityItemEdit.INTENT_MESSAGE_ITEM_VALUE_PRIORITY, m_spnrPriority.getSelectedItem().toString());

        setResult(RESULT_OK, rIntent);
        finish();
    }
}
