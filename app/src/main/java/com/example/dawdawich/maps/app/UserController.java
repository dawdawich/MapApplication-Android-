package com.example.dawdawich.maps.app;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.dawdawich.maps.helper.SQLiteHandler;

import java.util.Map;

public class UserController  {

    private static UserController instance;
    private String nickname;
    private String email;
    private SQLiteHandler db;

    private UserController(Context cnt) {
        db = new SQLiteHandler(cnt);
        Map<String, String> user = db.getUserDetails();
        nickname = user.get("nickname");
        email = user.get("email");
    }

    public synchronized static UserController getInstance(Context cnt)
    {
        if (instance == null)
        {
            instance = new UserController(cnt);
        }
        return instance;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
}
