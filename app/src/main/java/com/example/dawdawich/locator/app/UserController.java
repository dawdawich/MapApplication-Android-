package com.example.dawdawich.locator.app;


import android.content.Context;

import com.example.dawdawich.locator.data.User;
import com.example.dawdawich.locator.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserController   {

    private static UserController instance;
    private SQLiteHandler db;
    private User user;
    private User userPage;


    private UserController() {
        db = new SQLiteHandler(AppController.getInstance());
        Map<String, ?> user_data = AppController.getInstance().getSharedPreferences("user_data", Context.MODE_PRIVATE).getAll();
        user = new User((Integer) user_data.get("id"), (String) user_data.get("nickname"));
        user.setFriends(db.getFriends());
    }

    public synchronized static UserController getInstance()
    {
        if (instance == null)
        {
            instance = new UserController();
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

    public User parseUser (JSONObject jsonObject)
    {
        try {

            int id = jsonObject.getInt("id");
            String nickname = jsonObject.getString("nickname");
            Set<User> proposal;
            try {
                JSONArray incomingInvites = jsonObject.getJSONArray("incomingInvites");
                proposal = new HashSet<>();
                for (int i = 0; i < incomingInvites.length(); i++) {
                    proposal.add(new User(incomingInvites.getJSONObject(i).getInt("id"), incomingInvites.getJSONObject(i).getString("nickname")));
                }
            }
            catch (JSONException e)
            {
                proposal = null;
            }
            Set<User> waiting;
            try {
                JSONArray outcomingInvites = jsonObject.getJSONArray("outcomingcomingInvites");
                waiting = new HashSet<>();
                for (int i = 0; i < outcomingInvites.length(); i++) {
                    waiting.add(new User(outcomingInvites.getJSONObject(i).getInt("id"), outcomingInvites.getJSONObject(i).getString("nickname")));
                }
            }
            catch (JSONException e)
            {
                waiting = null;
            }
            Set<User> friends;
            try {
                JSONArray friendsU = jsonObject.getJSONArray("friends");
                friends = new HashSet<>();
                for (int i = 0; i < friendsU.length(); i++) {
                    friends.add(new User(friendsU.getJSONObject(i).getInt("id"), friendsU.getJSONObject(i).getString("nickname")));
                }
            }
            catch (JSONException e)
            {
                friends = null;
            }


            return new User(id, nickname, friends, waiting, proposal);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserPage() {
        return userPage;
    }

    public void setUserPage(User userPage) {
        this.userPage = userPage;
    }
}
