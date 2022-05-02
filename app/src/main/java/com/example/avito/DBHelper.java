package com.example.avito;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Authorization";

    public static final String TABLE_USERS = "Users";

    public static final String KEY_ID1 = "id1";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NAME = "name";

    public static final String TABLE_ANNOUNCEMENTS = "Announcements";

    public static final String KEY_ID2 = "id2";
    public static final String KEY_TITLE = "title";
    public static final String KEY_PRICE = "price";
    public static final String KEY_CATEGORYID = "categoryId";
    public static final String KEY_USERID = "userId";
    public static final String KEY_USERNAME = "userName";

    public static final String TABLE_CATEGORIES = "Categories";

    public static final String KEY_ID3 = "_id";
    public static final String KEY_CATEGORY = "category";

    public DBHelper(Context context) {super(context, DATABASE_NAME, null, DATABASE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + TABLE_USERS + "(" + KEY_ID1 + " integer primary key autoincrement,"
                + KEY_LOGIN + " text," + KEY_PASSWORD + " text," + KEY_NAME + " text)");

        db.execSQL("create table " + TABLE_ANNOUNCEMENTS + "(" + KEY_ID2 + " integer primary key autoincrement," + KEY_TITLE + " text,"
                + KEY_PRICE + " integer," + KEY_CATEGORYID + " integer,"+ KEY_USERID + " integer," + KEY_USERNAME + " text," + " foreign key(" + KEY_USERID + ") references "
                + TABLE_USERS + "(" + KEY_ID1 + ")," + " foreign key(" + KEY_CATEGORYID + ") references " + TABLE_CATEGORIES + "(" + KEY_ID3 + "))");

        db.execSQL("create table " + TABLE_CATEGORIES + "(" + KEY_ID3 + " integer primary key autoincrement," + KEY_CATEGORY + " text)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists " + TABLE_USERS);
        onCreate(db);

        db.execSQL("drop table if exists " + TABLE_ANNOUNCEMENTS);
        onCreate(db);

        db.execSQL("drop table if exists " + TABLE_CATEGORIES);
        onCreate(db);
    }
}