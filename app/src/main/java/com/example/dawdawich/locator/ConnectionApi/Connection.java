package com.example.dawdawich.locator.ConnectionApi;


import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.dawdawich.locator.app.AppController;

public class Connection  {

    private static Connection instance;
    private RequestQueue requestQueue;
    private static Context context;


    private Connection() {
        requestQueue = Volley.newRequestQueue(AppController.getInstance());
    }

    public static Connection getInstance() {
//        Connection.context = this.getApplicationContext();
        return instance == null ? instance = new Connection() : instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
