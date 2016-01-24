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
        public static final String COL_NAME_TITLE = "title";

        // SQL: Create
        public static final String SQL_CREATE =
                "CREATE TABLE " + ItemTable.TABLE_NAME + " ( " +
                        ItemTable.COL_NAME_TITLE + " TEXT " +
                        ")";

        // SQL: Delete
        public static final String SQL_DELETE =
                "DROP TABLE IF EXISTS " + ItemTable.TABLE_NAME;
    }
}
