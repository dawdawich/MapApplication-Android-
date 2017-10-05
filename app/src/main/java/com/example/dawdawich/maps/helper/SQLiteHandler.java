package com.example.dawdawich.maps.helper;

//get information from https://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLiteHandler extends SQLiteOpenHelper{

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    private static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    private static final String TABLE_USERS_POSITION = "users_positions";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NICKNAME = "nickname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_LAST_UPDATE = "last_update";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //cnt = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NICKNAME + " TEXT,"
                + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_CREATED_AT + " TEXT" + ")";

        String CREATE_USERS_POSITION_TABLE = "CREATE TABLE " + TABLE_USERS_POSITION +
                "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NICKNAME + " TEXT,"
                + KEY_LATITUDE + " REAL," + KEY_LONGITUDE + " REAL,"
                + KEY_LAST_UPDATE + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_USERS_POSITION_TABLE);

        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS_POSITION);


        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String nickname, String email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NICKNAME, nickname); // nickName
        values.put(KEY_EMAIL, email); // Email
        //values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("nickname", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("created_at", cursor.getString(3));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    public List<Map<String, Object>> getUsersPosition()
    {
        List<Map<String, Object>> users = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_USERS_POSITION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        while (cursor.moveToNext())
        {
            Map<String, Object> user = new HashMap<>();
            user.put("nickname", cursor.getString(1));
            user.put("latitude", cursor.getDouble(2));
            user.put("longitude", cursor.getDouble(3));
            user.put("last_update", cursor.getString(4));
            users.add(user);
        }
        cursor.close();
        db.close();

        return users;
    }

    public void updateUserPosition (String nickname, double latitudem, double longitude, String last_update)
    {

    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
