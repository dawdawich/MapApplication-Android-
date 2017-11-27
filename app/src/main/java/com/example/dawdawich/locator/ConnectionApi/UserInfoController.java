package com.example.dawdawich.locator.ConnectionApi;


import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dawdawich.locator.activity.LoginActivity;
import com.example.dawdawich.locator.app.AppConfig;
import com.example.dawdawich.locator.app.AppController;
import com.example.dawdawich.locator.app.UserController;
import com.example.dawdawich.locator.data.User;
import com.example.dawdawich.locator.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class UserInfoController {

    public static void getInfo(int user_id, User user) {
        JSONObject jsonObject = new JSONObject();
        URL url = null;

        try {
            jsonObject.put("user_id", user_id);
            url = new URL(AppConfig.URL_GETUSERINFO);


        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject,
                response -> {
                    try {
                        if (response.getBoolean("error"))
                        {
                            Toast.makeText(AppController.getInstance().getContext(),
                                    response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        Connection.getInstance().getRequestQueue().add(stringRequest);
    }

    public static void getThisInfo (int user_id)
    {
        JSONObject jsonObject = new JSONObject();
        URL url = null;

        try {
            jsonObject.put("id", user_id);
            url = new URL(AppConfig.URL_GETUSERINFO);



        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject,
                response -> {
            User user = null;
            try {
                if (response.getBoolean("error"))
                {
                    if (response.getString("error_msg").equals("User is null"))
                    {
                        AppController.getInstance().getSession().setLogin(false);
                        AppController.getInstance().startActivity(new Intent(AppController.getInstance().getContext(), LoginActivity.class));
                    }
                    Toast.makeText(AppController.getInstance().getContext(),
                            response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    return;
                }
                user = UserController.getInstance().parseUser(response.getJSONObject("user"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            User thisUser = UserController.getInstance().getUser();
            if (user == null || thisUser == null) return;
            if (user.getFriends() != null) thisUser.setFriends(user.getFriends()); else thisUser.getFriends().clear();
            if (user.getProposalFriends() != null)thisUser.setProposalFriends(user.getProposalFriends()); else thisUser.getProposalFriends().clear();
            if (user.getWaitingConfirmFriends() != null)thisUser.setWaitingConfirmFriends(user.getWaitingConfirmFriends()); else thisUser.getWaitingConfirmFriends().clear();
        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        Connection.getInstance().getRequestQueue().add(stringRequest);
    }

    public static void isUpdate (int id)
    {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", id);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_ISUPDATE, jsonObject, response -> {
            try {
                if (response.getBoolean("isUpdate") && UserController.getInstance().getUser().getId() == id)
                {
                    UserInfoController.getThisInfo(id);
                    if (AppController.getInstance().getUpdate() != null)
                    AppController.getInstance().getUpdate().updateData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        Connection.getInstance().getRequestQueue().add(jsonRequest);
    }

}
