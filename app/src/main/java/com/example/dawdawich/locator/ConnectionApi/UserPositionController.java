package com.example.dawdawich.locator.ConnectionApi;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dawdawich.locator.app.AppConfig;
import com.example.dawdawich.locator.app.AppController;
import com.example.dawdawich.locator.app.UserController;
import com.example.dawdawich.locator.data.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Set;

public class UserPositionController {

    public static void updateMyPosition (int user_id, double latitude, double longitude)
    {
        JSONObject jObject = new JSONObject();
        URL url = null;

        try {


            jObject.put("id", user_id);
            jObject.put("latitude", latitude);
            jObject.put("longitude", longitude);
            url = new URL(AppConfig.URL_UPDATEPOSITION);

            if (user_id == UserController.getInstance().getUser().getId()) {
                SharedPreferences prefs = AppController.getInstance().getSharedPreferences("user_data", Context.MODE_PRIVATE);
                prefs.edit().putFloat("user_latitude", (float) latitude).apply();
                prefs.edit().putFloat("user_longitude", (float) longitude).apply();
            }



        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        assert url != null;
        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jObject,
                response ->
                {
                    try {
                        if (response.getBoolean("error"))
                        {
                            Toast.makeText(AppController.getInstance().getContext(),
                                    response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error ->
                        Log.d("CONNECTION THREAD", "FAIL POST"));


        Connection.getInstance().getRequestQueue().add(stringRequest);
    }

    public static void getUserPosition (int user_id)
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
                    try {
                        if (response.getBoolean("error"))
                        {
                            Toast.makeText(AppController.getInstance().getContext(),
                                    response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Set<User> friends = UserController.getInstance().getUser().getFriends();
                        JSONObject user = response.getJSONObject("user");
                        int id = user.getInt("id");
                        for (User u : friends)
                        {
                            if (u.getId() == id)
                            {
                                u.setLatitude(user.getDouble("latitude"));
                                u.setLongitude(user.getDouble("longitude"));
                                AppController.getInstance().getDb().updateFriendPosition(u.getId(),
                                        u.getLatitude(), u.getLongitude());
                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.d("CONNECTION THREAD", "FAIL POST"));


        Connection.getInstance().getRequestQueue().add(stringRequest);
    }

}
