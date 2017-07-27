package com.example.dawdawich.maps.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User {

    private String nickname;
    private double latitude;
    private double longitude;
    private Date last_update;

    public User(String nickname, double latitude, double longitude, String last_update) {
        this.nickname = nickname;
        this.latitude = latitude;
        this.longitude = longitude;
        SimpleDateFormat parser = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        try {
            this.last_update = parser.parse(last_update);
        } catch (ParseException e) {
            e.printStackTrace();
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

    @Override
    public int hashCode() {
        return nickname.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).getNickname().equals(nickname);
    }
}
