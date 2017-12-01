package com.example.dawdawich.locator.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dawdawich.locator.data.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class SQLiteHandler extends SQLiteOpenHelper{

    private static final String TAG = SQLiteHandler.class.getSimpleName();



    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "locator_application";

    // Friends table
    private static final String TABLE_FRIENDS = "friends";

    private static final String KEY_ID = "id";
    private static final String KEY_FRIEND_ID = "friend_id";
    private static final String KEY_NICKNAME = "friend_nickname";
    private static final String KEY_LATITUDE = "friend_latitude";
    private static final String KEY_LONGITUDE = "friend_longitude";
    private static final String KEY_LAST_UPDATE = "friend_last_update";
    private static final String KEY_IMAGE_PATH = "friend_avatar_path";

    private static final String CREATE_FRIENDS_TABLE = "CREATE TABLE " + TABLE_FRIENDS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FRIEND_ID + " INTEGER,"
            + KEY_NICKNAME + " TEXT," + KEY_LATITUDE + " REAL," + KEY_LONGITUDE + " REAL,"
            + KEY_LAST_UPDATE + " TEXT," + KEY_IMAGE_PATH + " TEXT" + ")";

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

    public void addFriend (int friend_id, String username)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRIEND_ID, friend_id);
        values.put(KEY_NICKNAME, friend_id);

        db.insert(TABLE_FRIENDS, null, values);
        db.close();
    }

    public void addFriend (int friend_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FRIEND_ID, friend_id);

        db.insert(TABLE_FRIENDS, null, values);
        db.close();
    }

    public void updateFriendPosition (int friend_id, double latitude, double longitude)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);

        db.update(TABLE_FRIENDS, values, KEY_FRIEND_ID + "=" + friend_id, null);
        db.close();
    }

    public void updateFriendPath (int friend_id, String path)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_PATH, path);

        db.update(TABLE_FRIENDS, values, KEY_FRIEND_ID + "=" + friend_id, null);
        db.close();
    }

    public String getImageFriendPath (int friend_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_FRIENDS + " where friend_id=" + friend_id, null);
//        db.close();
        try {
            String avatar_name = cursor.getString(cursor.getColumnIndex(KEY_IMAGE_PATH));
            return avatar_name;
        }
        catch (CursorIndexOutOfBoundsException e)
        {
            return null;
        }
    }

    public double[] getFriendPosition (int friend_id) throws NullPointerException
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_FRIENDS + " where friend_id=" + friend_id, null);
//        db.close();
        double[] pos = new double[2];
        try {
            pos[0] = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE));
            pos[1] = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE));
        }
        catch (CursorIndexOutOfBoundsException e)
        {
            pos[0] = 0;
            pos[1] = 0;
        }
        return pos;
    }

    public void addFriends(JSONObject friends) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            JSONArray jsonArray = friends.getJSONArray("friends");
            ContentValues values;

            for (int i = 0; i < jsonArray.length(); i++) {

                if (!hasFriend(((JSONObject) jsonArray.get(i)).getInt("id"))) {
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

    public boolean hasFriend(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_FRIENDS + " where friend_id=" + id, null);
        return cursor != null && cursor.getCount() != 0;
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
