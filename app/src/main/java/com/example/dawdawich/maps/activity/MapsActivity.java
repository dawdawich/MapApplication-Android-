package com.example.dawdawich.maps.activity;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.dawdawich.maps.ConnectionApi.Connection;
import com.example.dawdawich.maps.R;
import com.example.dawdawich.maps.app.AppConfig;
import com.example.dawdawich.maps.app.AppController;
import com.example.dawdawich.maps.app.UserController;
import com.example.dawdawich.maps.data.User;
import com.example.dawdawich.maps.fragments.FriendsListFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private GoogleMap mMap;
    private double currentLatitude, currentLongitude;
    private String nickname;
    private MarkerOptions myMarker = new MarkerOptions();
    private IconGenerator iconGenerator;
    private Set<User> users_pos;
    private Map<String, Marker> users_marker;

    private static final int ACCESS_FINE_LOCATION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;

    private Handler handler;

    private boolean wait = false;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private FriendsListFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.maps_drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.maps_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        nickname = UserController.getInstance(this).getNickname();

        iconGenerator = new IconGenerator(this);


        myMarker.position(new LatLng(0, 0));
        myMarker.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(nickname)));

        users_pos = new HashSet<>();
        users_marker = new HashMap<>();


        handler = new Handler() {

            public void handleMessage(Message msg) {

                String json = msg.getData().getString("message");

                try {
                    JSONObject jObj = new JSONObject(json);

                    JSONArray users = jObj.getJSONArray("users");

                    Object o;
                    for (int i = 0; i < users.length(); i++) {
                        o = users.get(i);
                        if (o instanceof JSONObject) {
                            JSONObject u = (JSONObject) o;
                            if (u.getString("nickname").equals(nickname))
                                continue;
                            users_pos.add(new User(u.getString("nickname"), u.getDouble("player_position_latitude"),
                                    u.getDouble("player_position_longitude"), u.getString("last_update")));
                        }
                    }


                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "Json exception in parse users position.", Toast.LENGTH_LONG).show();
                }

                if (mMap != null) {
                    for (User user : users_pos) {
                        if (users_marker.get(user.getNickname()) == null) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(user.getLatitude(), user.getLongitude()));
                            markerOptions.icon(BitmapDescriptorFactory.
                                    fromBitmap(iconGenerator.makeIcon(user.getNickname())));

                            users_marker.put(user.getNickname(), mMap.addMarker(markerOptions));
                        } else {
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

                    wait = false;

                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        background.start();

    }

    private void proceedAfterPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(), "Your haven't a location permission.", Toast.LENGTH_LONG).show();
            return;
        }
        final Marker marker = mMap.addMarker(myMarker);


        mMap.setMyLocationEnabled(true);

        final Context cnt = this;

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {

                currentLatitude = arg0.getLatitude();
                currentLongitude = arg0.getLongitude();



                marker.setPosition(new LatLng(currentLatitude, currentLongitude));

                if (!wait) {

                    JSONObject temporary = new JSONObject();
                    try {
                        temporary.put("nickname", "dawdawich");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST,
                            AppConfig.URL_GETPOSITIONS, temporary, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Message msgObj = handler.obtainMessage();
                            Bundle b = new Bundle();
                            b.putString("message", response.toString());
                            msgObj.setData(b);
                            handler.sendMessage(msgObj);

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    AppController.getInstance().addToRequestQueue(strReq, "req_pos");


                    Connection.getInstance(cnt).updateMyPosition(nickname,
                            currentLatitude, currentLongitude);

                    wait = true;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_FINE_LOCATION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Access Location Permission");
                builder.setMessage("This app needs to detect your location.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.ACCESS_FINE_LOCATION,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Access Location Permission");
                builder.setMessage("This app needs to detect your location.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            } else {
                //just request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.ACCESS_FINE_LOCATION,true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();

        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.friend)
        {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragment = fragment == null ? new FriendsListFragment() : fragment;
            fragmentTransaction.replace(R.id.maps_drawer_layout, fragment, "friends_fragment");
            fragmentTransaction.addToBackStack("friends_fragment").commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.maps_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.maps_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(fragment != null && fragment.isVisible())
        {
            getSupportFragmentManager().popBackStackImmediate();
            getSupportActionBar().setDisplayShowCustomEnabled(false);
        } else {
            super.onBackPressed();
        }
    }



}
