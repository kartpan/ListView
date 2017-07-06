package com.example.karthik.listview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by karthik on 30/6/17.
 */

public class NewsDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NewsDB.db";
    private static final int DATABASE_VERSION = 1;
    public static final String NEWS_TABLE_NAME = "news";
    public static final String NEWS_COLUMN_ID = "_id";
    public static final String NEWS_COLUMN_TITLE = "title";
    public static final String NEWS_COLUMN_SUBTITLE = "subtitle";
    public static final String NEWS_COLUMN_URL = "url";
    public static final String NEWS_COLUMN_IMAGE_URL = "imageurl";


    public NewsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + NEWS_TABLE_NAME + "(" +
                NEWS_COLUMN_ID + " INTEGER PRIMARY KEY, " +
                NEWS_COLUMN_TITLE + " TEXT, " +
                NEWS_COLUMN_SUBTITLE + " TEXT, " +
                NEWS_COLUMN_URL + " TEXT," +
                NEWS_COLUMN_IMAGE_URL + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NEWS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNews(String title, String subtitle, String url, String imageurl) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS_COLUMN_TITLE, title);
        contentValues.put(NEWS_COLUMN_SUBTITLE, subtitle);
        contentValues.put(NEWS_COLUMN_URL, url);
        contentValues.put(NEWS_COLUMN_IMAGE_URL, imageurl);
        db.insert(NEWS_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateNews(Integer id, String title, String subtitle, String url, String imageurl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS_COLUMN_TITLE, title);
        contentValues.put(NEWS_COLUMN_SUBTITLE, subtitle);
        contentValues.put(NEWS_COLUMN_URL, url);
        contentValues.put(NEWS_COLUMN_IMAGE_URL, imageurl);
        db.update(NEWS_TABLE_NAME, contentValues, NEWS_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Cursor getNews(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + NEWS_TABLE_NAME + " WHERE " +
                NEWS_COLUMN_ID + "=?", new String[]{Integer.toString(id)});
        return res;
    }

    public Cursor getAllNews() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + NEWS_TABLE_NAME, null);
        return res;
    }

    public Integer deleteNews(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NEWS_TABLE_NAME,
                NEWS_COLUMN_ID + " = ? ",
                new String[]{Integer.toString(id)});
    }

    public Integer deleteAllNews() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(NEWS_TABLE_NAME, null, null);
    }


}
