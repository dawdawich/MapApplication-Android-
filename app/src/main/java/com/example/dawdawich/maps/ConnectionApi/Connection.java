package com.example.dawdawich.maps.ConnectionApi;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Connection {

    private static Connection instance;
    private RequestQueue requestQueue;
    private String ipAddress = "testlaravel.hopto.org:8000";
    private static Context context;

    private Connection() {
        requestQueue = Volley.newRequestQueue(context);
    }

    public static Connection getInstance(Context context) {
        Connection.context = context;
        return instance == null ? instance = new Connection() : instance;
    }

    public void sendSearchPost (String nickname, double latitude, double longitude)
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

    }

    public void test ()
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

    }

    public void updateMyPosition (String nickname, double latitude, double longitude)
    {
        JSONObject jObject = new JSONObject();
        URL url = null;

        try {
            Date myDate = new Date();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
            calendar.setTime(myDate);
            Date time = calendar.getTime();
            SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String dateAsString = outputFmt.format(time);


            jObject.put("nickname", nickname);
            jObject.put("latitude", latitude);
            jObject.put("longitude", longitude);
            jObject.put("last_update", dateAsString);
            url = new URL("http://" + ipAddress + "/updatePosition");



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
    }

}
