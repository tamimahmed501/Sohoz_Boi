package com.ebook.sohozboi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ebook1.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "texts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BOOK_NAME = "book_name";
    public static final String COLUMN_TEXT = "text";
    public static final String IMAGE_BITMAP = "bitmap";
    public static final String AUTHOR_NAME = "author_name";  // Corrected from Bitmap to String

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_BOOK_NAME + " TEXT, " +
                    COLUMN_TEXT + " TEXT, " +
                    AUTHOR_NAME + " TEXT, " +   // Corrected
                    IMAGE_BITMAP + " TEXT);";   // Corrected

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
