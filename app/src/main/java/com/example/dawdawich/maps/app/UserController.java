package com.example.dawdawich.maps.app;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.support.annotation.Nullable;

import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.data.User;
import com.example.dawdawich.maps.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Permission;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserController  {

    private static UserController instance;
    private SQLiteHandler db;
    private User user;
    private User userPage;


    private UserController(Context cnt) {
        db = new SQLiteHandler(cnt);
        Map<String, ?> user_data = cnt.getSharedPreferences("user_data", Context.MODE_PRIVATE).getAll();
        user = new User((Integer) user_data.get("user_id"), (String) user_data.get("user_nickname"));
        user.setFriends(db.getFriends());
    }

    public synchronized static UserController getInstance(Context cnt)
    {
        if (instance == null)
        {
            instance = new UserController(cnt);
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    public Set<User> parseUsers (JSONArray jsonArray)
    {
        JSONObject user;
        Set<User> users = new HashSet<>();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            try {
                user = jsonArray.getJSONObject(i);

                users.add(new User(user.getInt("id"), user.getString("nickname")));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return users;
    }

    public User getUserPage() {
        return userPage;
    }

    public void setUserPage(User userPage) {
        this.userPage = userPage;
    }
}
