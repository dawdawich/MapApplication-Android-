package com.example.dawdawich.maps.helper;

//get information from https://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dawdawich.maps.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SQLiteHandler extends SQLiteOpenHelper{

    private static final String TAG = SQLiteHandler.class.getSimpleName();



    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "map_application";

    // Friends table
    private static final String TABLE_FRIENDS = "friends";

    private static final String KEY_ID = "id";
    private static final String KEY_FRIEND_ID = "friend_id";
    private static final String KEY_NICKNAME = "friend_nickname";
    private static final String KEY_LATITUDE = "friend_latitude";
    private static final String KEY_LONGITUDE = "friend_longitude";
    private static final String KEY_LAST_UPDATE = "friend_last_update";
    private static final String KEY_IMAGE = "friend_avatar";

    private static final String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FRIEND_ID + "INTEGER,"
            + KEY_NICKNAME + " TEXT," + KEY_LATITUDE + " REAL," + KEY_LONGITUDE + " REAL,"
            + KEY_LAST_UPDATE + " TEXT," + KEY_IMAGE + " BLOB" + ")";

    //------------------------------------------------------


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_FRIENDS_TABLE);

//        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);

        // Create tables again
        onCreate(db);
    }

    public void addFriends(JSONObject friends) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            JSONArray jsonArray = friends.getJSONArray("friends");
            ContentValues values;

            for (int i = 0; i < jsonArray.length(); i++) {

                if (!hasFriend(((JSONObject) jsonArray.get(i)).getInt("id"), db)) {
                    values = new ContentValues();
                    values.put(KEY_FRIEND_ID, ((JSONObject) jsonArray.get(i)).getInt("id"));
                    values.put(KEY_NICKNAME, ((JSONObject) jsonArray.get(i)).getInt("nickname"));

                    db.insert(TABLE_FRIENDS, null, values);
                }
            }

            db.close(); // Closing database connection
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean hasFriend(int id, SQLiteDatabase db)
    {
        Cursor cursor = db.rawQuery("select * from " + TABLE_FRIENDS + "where friend_id=" + id, null);
        return cursor != null;
    }

    public Set<User> getFriends()
    {
        Set<User> users = new HashSet<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FRIENDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        while (cursor.moveToNext())
        {
            users.add(new User(cursor.getInt(1), cursor.getString(2)));
        }
        cursor.close();
        db.close();

        return users;
    }


}
