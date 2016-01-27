package com.enlaps.m.and.todo;

import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by Atom on 1/24/16.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public static final int     DB_VERSION = 2;
    public static final String  DB_NAME = "todo.db";

    private SQLiteDatabase  m_db;

    public SqliteHelper(Context context) {
        super( context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchema.ItemTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i("Drop", DBSchema.ItemTable.SQL_DELETE);
        Log.i("Crea", DBSchema.ItemTable.SQL_CREATE);

        db.execSQL(DBSchema.ItemTable.SQL_DELETE);
        db.execSQL(DBSchema.ItemTable.SQL_CREATE);
    }

    protected boolean setDB(SQLiteDatabase db) {

        if( null == db) {
            return false;
        }

        m_db = db;
        return true;
    }

    protected long insertRow(String itemName) {

        ContentValues rowValues = new ContentValues();

        rowValues.put(DBSchema.ItemTable.COL_NAME_TITLE, itemName);

        return m_db.insert(DBSchema.ItemTable.TABLE_NAME, null, rowValues);
    }

    protected long insertRow(String itemName, Date date, String priority) {

        ContentValues rowValues = new ContentValues();

        // Date
            SimpleDateFormat dateFormatMMDDYY = new SimpleDateFormat("MM-dd-yyyy");

        Log.d("New Row: Date", dateFormatMMDDYY.format(date));

        rowValues.put(DBSchema.ItemTable.COL_NAME_TITLE, itemName);
        rowValues.put(DBSchema.ItemTable.COL_NAME_DUE_DATE, dateFormatMMDDYY.format(date));
        rowValues.put(DBSchema.ItemTable.COL_NAME_PRIORITY, priority);

        return m_db.insert(DBSchema.ItemTable.TABLE_NAME, null, rowValues);
    }

    protected int deleteRow(String itemName) {

        String selection        = DBSchema.ItemTable.COL_NAME_TITLE + " LIKE ?";
        String selectionArgs[]  = {itemName};

        return m_db.delete(DBSchema.ItemTable.TABLE_NAME, selection, selectionArgs);
    }

    protected int deleteRow(int id) {

        String selection        = DBSchema.ItemTable.COL_NAME_ID + "=" + id;

        return m_db.delete(DBSchema.ItemTable.TABLE_NAME, selection, null);
    }

    protected int updateRow(String itemNameOld, String itemNameNew) {

        String  selection       = DBSchema.ItemTable.COL_NAME_TITLE + " LIKE ?";
        String  selectionArgs[] = {itemNameOld};
        ContentValues   rowValues = new ContentValues();

        rowValues.put(DBSchema.ItemTable.COL_NAME_TITLE, itemNameNew);

        return m_db.update(DBSchema.ItemTable.TABLE_NAME, rowValues, selection, selectionArgs);
    }
    protected int updateRow(int id, String itemName, Date date, String priority) {

        String  selection       = DBSchema.ItemTable.COL_NAME_ID + " = " + id;
        ContentValues   rowValues = new ContentValues();

        // Date
        SimpleDateFormat dateFormatMMDDYY = new SimpleDateFormat("MM-dd-yyyy");

        rowValues.put(DBSchema.ItemTable.COL_NAME_ID, id);
        rowValues.put(DBSchema.ItemTable.COL_NAME_TITLE, itemName);
        rowValues.put(DBSchema.ItemTable.COL_NAME_DUE_DATE, dateFormatMMDDYY.format(date));
        rowValues.put(DBSchema.ItemTable.COL_NAME_PRIORITY, priority);

        return m_db.update(DBSchema.ItemTable.TABLE_NAME, rowValues, selection, null);
    }
}
