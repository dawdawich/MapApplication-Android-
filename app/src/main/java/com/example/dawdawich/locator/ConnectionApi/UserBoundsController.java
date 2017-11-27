package com.example.dawdawich.locator.ConnectionApi;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dawdawich.locator.activity.UserPageActivity;
import com.example.dawdawich.locator.app.AppConfig;
import com.example.dawdawich.locator.app.AppController;
import com.example.dawdawich.locator.app.UserController;
import com.example.dawdawich.locator.data.User;
import com.example.dawdawich.locator.fragments.FriendsSearchFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class UserBoundsController {

    public static void searchUsers(String nickname)
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
                if (response.getBoolean("error"))
                {
                    Toast.makeText(AppController.getInstance().getContext(),
                            response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                }
                FriendsSearchFragment.changeList(UserController.getInstance().parseUsers(response.getJSONArray("friends")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        Connection.getInstance().getRequestQueue().add(stringRequest);
    }

    public static void sendInvite(int this_user_id, int current_user_id)
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

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject,
                response -> {
            User friend = null;
            try {
                if (response.getBoolean("error"))
                {
                    Toast.makeText(AppController.getInstance().getContext(),
                            response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    return;
                }
                friend = UserController.getInstance().parseUser(response.getJSONObject("friend"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UserController.getInstance().getUser().getWaitingConfirmFriends().add(friend);
            UserPageActivity.setWaiting();
        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        Connection.getInstance().getRequestQueue().add(stringRequest);
    }

    public static void confirmInvite(int this_user_id, int current_user_id)
    {
        JSONObject jsonObject = new JSONObject();
        URL url = null;

        try {
            jsonObject.put("user_to_id", this_user_id);
            jsonObject.put("user_from_id", current_user_id);
            url = new URL(AppConfig.URL_CONFIRMINVITE);



        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject,
                response -> {
            User friend = null;
            try {
                if (response.getBoolean("error"))
                {
                    Toast.makeText(AppController.getInstance().getContext(),
                            response.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    return;
                }
                friend = UserController.getInstance().parseUser(response.getJSONObject("friend"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            UserController.getInstance().getUser().getFriends().add(friend);
            UserController.getInstance().getUser().getProposalFriends().remove(friend);
            UserPageActivity.setFriend();
        }, error -> Log.d("CONNECTION THREAD", "FAIL POST"));

        Connection.getInstance().getRequestQueue().add(stringRequest);
    }

}
