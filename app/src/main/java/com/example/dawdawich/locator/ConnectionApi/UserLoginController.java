package com.example.dawdawich.locator.ConnectionApi;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dawdawich.locator.app.AppConfig;
import com.example.dawdawich.locator.helper.MD5Hashing;

import org.json.JSONException;
import org.json.JSONObject;

public class UserLoginController {

    public static void login (String nickname, String password, Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener)
    {
        JSONObject params = new JSONObject();
        try {
            String newPass = MD5Hashing.md5(password);
            params.put("nickname", nickname);
            params.put("password", newPass);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, params, listener, errorListener);

        Connection.getInstance().getRequestQueue().add(strReq);
    }

    public static void register (String nickname, String password, String email,
                                 Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
    {
        JSONObject params = new JSONObject();
        try {
            String newPass = MD5Hashing.md5(password);
            params.put("nickname", nickname);
            params.put("password", newPass);
            params.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, params, listener, errorListener);

        Connection.getInstance().getRequestQueue().add(strReq);

    }

}
















