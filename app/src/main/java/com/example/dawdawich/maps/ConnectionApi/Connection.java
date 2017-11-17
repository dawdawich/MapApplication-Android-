package com.example.dawdawich.maps.ConnectionApi;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.dawdawich.maps.activity.UserPageActivity;
import com.example.dawdawich.maps.app.AppConfig;
import com.example.dawdawich.maps.app.AppController;
import com.example.dawdawich.maps.app.UserController;
import com.example.dawdawich.maps.data.User;
import com.example.dawdawich.maps.fragments.FriendsSearchFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class Connection {

    private static Connection instance;
    private RequestQueue requestQueue;
    private static Context context;

    private Connection() {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static Connection getInstance(Context context) {
        Connection.context = context;
        return instance == null ? instance = new Connection() : instance;
    }

/*    public void sendSearchPost (String nickname, double latitude, double longitude)
    {
        JSONObject jObject = new JSONObject();
        URL url = null;

        try {
            jObject.put("nickname", nickname);
            jObject.put("latitude", latitude);
            jObject.put("longitude", longitude);
            url = new URL("http://" + ipAddress + "/searchOpponent");



        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jObject, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("CONNECTION THREAD", "SUCCESS POST");

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CONNECTION THREAD", "FAIL POST");
            }
        });


        requestQueue.add(stringRequest);

    }*/

/*    public void test ()
    {
        JSONObject jObject = new JSONObject();
        URL url = null;

        try {

            url = new URL("http://" + ipAddress + "/test");



        } catch (IOException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("CONNECTION THREAD", "SUCCESS POST");

            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("CONNECTION THREAD", "FAIL POST");
            }
        });


        requestQueue.add(stringRequest);

    }*/

    public void updateMyPosition (int user_id, double latitude, double longitude)
    {
        JSONObject jObject = new JSONObject();
        URL url = null;

        try {


            jObject.put("id", user_id);
            jObject.put("latitude", latitude);
            jObject.put("longitude", longitude);
            url = new URL(AppConfig.URL_UPDATEPOSITION);



        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jObject, response -> Log.d("CONNECTION THREAD", "SUCCESS POST"), error -> Log.d("CONNECTION THREAD", "FAIL POST"));


        requestQueue.add(stringRequest);
    }

    public void getUserPosition (int user_id)
    {
        JSONObject jObject = new JSONObject();
        URL url = null;

        try {


            jObject.put("id", user_id);
            url = new URL(AppConfig.URL_GETPOSITION);



        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jObject,
                response -> {
                    Set<User> friends = UserController.getInstance(context).getUser().getFriends();
                    try {
                        JSONObject user = response.getJSONObject("user");
                        int id = user.getInt("id");
                        for (User u : friends)
                        {
                            if (u.getId() == id)
                            {
                                u.setLatitude(user.getDouble("latitude"));
                                u.setLongitude(user.getDouble("longitude"));
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.d("CONNECTION THREAD", "FAIL POST"));


        requestQueue.add(stringRequest);
    }

    public void getInfo (int user_id, User user)
    {
        JSONObject jsonObject = new JSONObject();
        URL url = null;

        try {
            jsonObject.put("user_id", user_id);
            url = new URL(AppConfig.URL_GETUSERINFO);



        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject, response -> {

        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        requestQueue.add(stringRequest);
    }

    public void getThisInfo (int user_id)
    {
        JSONObject jsonObject = new JSONObject();
        URL url = null;

        try {
            jsonObject.put("id", user_id);
            url = new URL(AppConfig.URL_GETUSERINFO);



        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject, response -> {
            User user = null;
            try {
                user = UserController.getInstance(context).parseUser(response.getJSONObject("user"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            User thisUser = UserController.getInstance(context).getUser();
            thisUser.setFriends(user.getFriends());
            thisUser.setProposalFriends(user.getProposalFriends());
            thisUser.setWaitingConfirmFriends(user.getWaitingConfirmFriends());
        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        requestQueue.add(stringRequest);
    }

    public void searchUsers(String nickname)
    {
        JSONObject jsonObject = new JSONObject();
        URL url = null;

        try {
            jsonObject.put("nickname", nickname);
            url = new URL(AppConfig.URL_SEARCHUSERS);



        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject, response -> {
            try {
                FriendsSearchFragment.changeList(UserController.getInstance(context).parseUsers(response.getJSONArray("friends")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        requestQueue.add(stringRequest);
    }

    public void sendInvite(int this_user_id, int current_user_id)
    {
        JSONObject jsonObject = new JSONObject();
        URL url = null;

        try {
            jsonObject.put("this_user_id", this_user_id);
            jsonObject.put("current_user_id", current_user_id);
            url = new URL(AppConfig.URL_SENDINVITE);



        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject, response -> {
            User friend = null;
            try {
                friend = UserController.getInstance(context).parseUser(response.getJSONObject("friend"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UserController.getInstance(context).getUser().getWaitingConfirmFriends().add(friend);
            UserPageActivity.setWaiting();
        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        requestQueue.add(stringRequest);
    }

    public void confirmInvite(int this_user_id, int current_user_id)
    {
        JSONObject jsonObject = new JSONObject();
        URL url = null;

        try {
            jsonObject.put("this_user_id", this_user_id);
            jsonObject.put("current_user_id", current_user_id);
            url = new URL(AppConfig.URL_CONFIRMINVITE);



        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject, response -> {
            User friend = null;
            try {
                friend = UserController.getInstance(context).parseUser(response.getJSONObject("friend"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UserController.getInstance(context).getUser().getFriends().add(friend);
            UserPageActivity.setFriend();
        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        requestQueue.add(stringRequest);
    }

}
