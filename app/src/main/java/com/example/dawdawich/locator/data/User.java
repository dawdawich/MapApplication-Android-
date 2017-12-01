package com.example.dawdawich.locator.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.example.dawdawich.locator.app.AppController;
import com.example.dawdawich.locator.helper.ImageHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class User {

    private int id;
    private String nickname;
    private double latitude;
    private double longitude;
    private Date last_update;
    private Set<User> friends;
    private Set<User> waitingConfirmFriends;
    private Set<User> proposalFriends;
    private Bitmap avatar;


    public User(int id, String nickname, double latitude, double longitude, String last_update) {
        this.id = id;
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        SimpleDateFormat parser = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        try {
            this.last_update = parser.parse(last_update);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        friends = new HashSet<>();
        waitingConfirmFriends = new HashSet<>();
        proposalFriends = new HashSet<>();

    }

    public User(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;

        SharedPreferences prefs = AppController.getInstance().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        if (id == prefs.getInt("id", 0)) {
            latitude = prefs.getFloat("user_latitude", 0);
            longitude = prefs.getFloat("user_longitude", 0);
            String path = prefs.getString("avatar_path", "");
            if (!path.equals("") && avatar == null) {
                File file = new File(AppController.getInstance().getFilesDir(), path);
                try {
                    BufferedInputStream br = new BufferedInputStream (new FileInputStream(file));
                    long size = file.length();
                    byte[] buffer = new byte[(int) size];
                    br.read(buffer, 0, buffer.length);
                    br.close();
                    avatar = ImageHelper.getImage(buffer);
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }
            }
        }

        friends = new HashSet<>();
        waitingConfirmFriends = new HashSet<>();
        proposalFriends = new HashSet<>();
    }

    public User(int id, String nickname, Set<User> friends, Set<User> waitingConfirmFriends, Set<User> proposalFriends) {
        this.id = id;
        this.nickname = nickname;
        this.friends = friends;
        this.waitingConfirmFriends = waitingConfirmFriends;
        this.proposalFriends = proposalFriends;

        SharedPreferences prefs = AppController.getInstance().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        if (id == prefs.getInt("id", 0)) {
            latitude = prefs.getFloat("user_latitude", 0);
            longitude = prefs.getFloat("user_longitude", 0);
            String path = prefs.getString("avatar_path", "");
            if (!path.equals("") && avatar == null) {
                File file = new File(AppController.getInstance().getFilesDir(), path);
                try {
                    BufferedInputStream br = new BufferedInputStream (new FileInputStream(file));
                    long size = file.length();
                    byte[] buffer = new byte[(int) size];
                    br.read(buffer, 0, buffer.length);
                    br.close();
                    avatar = ImageHelper.getImage(buffer);
                }
                catch (IOException e) {
                    //You'll need to add proper error handling here
                }
            }
        }

    }

    public String getNickname() {
        return nickname;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getLast_update() {
        return last_update;
    }

    public void updateUser (JSONObject data)
    {
        try {
            if (!data.getString("nickname").equals(nickname))
            {
                nickname = data.getString("nickname");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLast_update(Date last_update) {
        this.last_update = last_update;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public void setWaitingConfirmFriends(Set<User> waitingConfirmFriends) {
        this.waitingConfirmFriends = waitingConfirmFriends;
    }

    public void setProposalFriends(Set<User> proposalFriends) {
        this.proposalFriends = proposalFriends;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public Set<User> getWaitingConfirmFriends() {
        return waitingConfirmFriends;
    }

    public Set<User> getProposalFriends() {
        return proposalFriends;
    }

    public int getId() {
        return id;
    }

    @Override
    public int hashCode() {
        return nickname.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).getNickname().equals(nickname);
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
