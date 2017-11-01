package com.example.dawdawich.maps.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    }

    public User(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
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

    @Override
    public int hashCode() {
        return nickname.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof User && ((User) obj).getNickname().equals(nickname);
    }
}
