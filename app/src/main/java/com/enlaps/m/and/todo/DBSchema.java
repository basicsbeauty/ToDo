package com.enlaps.m.and.todo;

import android.provider.BaseColumns;

/**
 * Created by Atom on 1/24/16.
 */
public class DBSchema {

    public DBSchema() {}

    public static abstract class ItemTable implements BaseColumns {

        // Table
        public static final String TABLE_NAME = "item";

        // Columns
        public static final String COMMA = ",";

        // Columns
        public static final String COL_NAME_ID = "id";
        public static final String COL_NAME_TITLE = "title";
        public static final String COL_NAME_PRIORITY = "priority";
        public static final String COL_NAME_DUE_DATE = "due_date";

        // SQL: Create
        public static final String SQL_CREATE =
                "CREATE TABLE " + ItemTable.TABLE_NAME + " ( " +
                        ItemTable.COL_NAME_ID       + " INTEGER PRIMARY KEY" + ItemTable.COMMA +
                        ItemTable.COL_NAME_TITLE    + " TEXT " + ItemTable.COMMA +
                        ItemTable.COL_NAME_DUE_DATE + " DATE " + ItemTable.COMMA +
                        ItemTable.COL_NAME_PRIORITY + " TEXT " +
                        ")";

        // SQL: Delete
        public static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + ItemTable.TABLE_NAME;
    }
}
