package com.example.arysugiarto.kumatoapps.db;

import android.database.Cursor;
import android.provider.BaseColumns;


public class DatabaseContract {


    static final class NoteColumns implements BaseColumns {
        static String TABLE_NAME = "note";

        //Note title
        static String TITLE = "title";
        //Note description
        static String DESCRIPTION = "description";
        //Note date
        static String DATE = "date";
        static String ONDATE = "date";
        static String ONTIME = "date";

    }

}
