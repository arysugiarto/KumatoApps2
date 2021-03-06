package com.example.arysugiarto.kumatoapps.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.arysugiarto.kumatoapps.db.DatabaseContract.NoteColumns;

import static com.example.arysugiarto.kumatoapps.db.DatabaseContract.NoteColumns.TABLE_NAME;


public class  DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbkumato";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_NAME,
            NoteColumns._ID,
            NoteColumns.TITLE,
            NoteColumns.DESCRIPTION,
            NoteColumns.DATE,
            NoteColumns.ONDATE,
            NoteColumns.ONTIME
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        Drop table tidak dianjurkan ketika proses migrasi terjadi dikarenakan data user akan hilang,
         */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
