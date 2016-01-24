package com.enlaps.m.and.todo;

import android.content.ClipData;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Atom on 1/24/16.
 */
public class SqliteHelper extends SQLiteOpenHelper {

    public static final int     DB_VERSION = 1;
    public static final String  DB_NAME = "todo.db";

    public SqliteHelper(Context context) {
        super( context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBSchema.ItemTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBSchema.ItemTable.SQL_DELETE);
        db.execSQL(DBSchema.ItemTable.SQL_CREATE);
    }

}
