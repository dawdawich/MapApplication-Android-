package com.example.dawdawich.maps.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dawdawich.maps.ConnectionApi.Connection;
import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.app.AppConfig;
import com.example.dawdawich.maps.app.AppController;
import com.example.dawdawich.maps.app.UserController;
import com.example.dawdawich.maps.data.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double currentLatitude, currentLongitude;
    private String nickname;
    private MarkerOptions myMarker = new MarkerOptions();
    private IconGenerator iconGenerator;
    private Set<User> users_pos;
    private Map<String, Marker> users_marker;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        nickname = UserController.getInstance(this).getNickname();

        iconGenerator = new IconGenerator(this);



        myMarker.position(new LatLng(0, 0));
        myMarker.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(nickname)));

        users_pos = new HashSet<>();
        users_marker = new HashMap<>();



        handler =  new Handler() {

            public void handleMessage(Message msg) {

                String json = msg.getData().getString("message");

                try {
                    JSONObject jObj = new JSONObject(json);

                    JSONArray users = jObj.getJSONArray("users");

                    Object o;
                    for (int i = 0; i < users.length(); i++)
                    {
                        o = users.get(i);
                        if (o instanceof JSONObject)
                        {
                            JSONObject u = (JSONObject)o;
                            if (u.getString("nickname").equals(nickname))
                                continue;
                            users_pos.add(new User(u.getString("nickname"), u.getDouble("player_position_latitude"),
                                    u.getDouble("player_position_longitude"), u.getString("last_update")));
                        }
                    }




                } catch (JSONException e) {

                }

                if (mMap != null) {
                    for (User user : users_pos) {
                        if (users_marker.get(user.getNickname()) == null) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(user.getLatitude(), user.getLongitude()));
                            markerOptions.icon(BitmapDescriptorFactory.
                                    fromBitmap(iconGenerator.makeIcon(user.getNickname())));

                            users_marker.put(user.getNickname(), mMap.addMarker(markerOptions));
                        }
                        else
                        {
                            users_marker.get(user.getNickname()).
                                    setPosition(new LatLng(user.getLatitude(), user.getLongitude()));
                        }
                    }
                }

            }
        };

        Thread background = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    StringRequest strReq = new StringRequest(Request.Method.GET,
                            AppConfig.URL_GETPOSITIONS, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message", response);
                            msgObj.setData(b);
                            handler.sendMessage(msgObj);

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    AppController.getInstance().addToRequestQueue(strReq, "req_pos");

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        background.start();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        final Marker marker = mMap.addMarker(myMarker);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        final Context cnt = this;

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {
                // TODO Auto-generated method stub



                /*CircleOptions circle = new CircleOptions();
                circle.center(me.getPosition());
                circle.radius(1000);
                circle.fillColor(Color.BLUE);*/



                currentLatitude = arg0.getLatitude();
                currentLongitude = arg0.getLongitude();



                marker.setPosition(new LatLng(currentLatitude, currentLongitude));



                Connection.getInstance(cnt).updateMyPosition(nickname,
                        currentLatitude, currentLongitude);
            }
        });

    }




}
