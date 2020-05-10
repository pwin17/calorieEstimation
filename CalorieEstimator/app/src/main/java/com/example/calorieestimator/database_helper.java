package com.example.calorieestimator;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class database_helper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME="calories.db";
    public static final String TABLE_NAME="credentials";
    public static final String COL_1="ID";
    public static final String COL_2="FirstName";
    public static final String COL_3="LastName";
    public static final String COL_4="email";
    public static final String COL_5="password";

    public database_helper(Context context){
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT, email TEXT, first_name TEXT,last_name TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
