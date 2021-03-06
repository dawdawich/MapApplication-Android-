package com.example.dawdawich.locator.activity;

//get information from https://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.dawdawich.locator.ConnectionApi.UserLoginController;
import com.example.dawdawich.locator.R;
import com.example.dawdawich.locator.app.AppController;
import com.example.dawdawich.locator.helper.SQLiteHandler;
import com.example.dawdawich.locator.helper.SessionManager;


public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SQLiteHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        String nickname = getSharedPreferences("user_data", MODE_PRIVATE).getString("nickname", "");

        // Session manager

        // Check if user is already logged in or not
        if (AppController.getInstance().getSession().isLoggedIn() && !nickname.equals("")) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MapsActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(view -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            // Check for empty data in the form
            if (!email.isEmpty() && !password.isEmpty()) {
                // login user
                checkLogin(email, password);
            } else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(),
                        "Please enter the credentials!", Toast.LENGTH_LONG)
                        .show();
            }
        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(),
                    RegisterActivity.class);
            startActivity(i);
            finish();
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(String nickname, String password) {

        pDialog.setMessage("Logging in ...");
        showDialog();

        Response.Listener<JSONObject> listener = (response) -> {
            // Tag used to cancel the request
            Log.d(TAG, "Login Response: " + response.toString());
            hideDialog();

            try {
                boolean error = response.getBoolean("error");

                // Check for error node in json
                if (!error) {
                    // user successfully logged in
                    // Create login session
                    JSONObject user = response.getJSONObject("user");
                    //session.setLogin(true);

                    SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                    preferences.edit().putInt("id", user.getInt("id")).apply();
                    preferences.edit().putString("nickname", user.getString("nickname")).apply();
                    preferences.edit().putString("email", user.getString("email")).apply();

                    AppController.getInstance().getSession().setLogin(true);

                    // Launch main activity
                    Intent intent = new Intent(LoginActivity.this,
                            MapsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Error in login. Get the error message
                    String errorMsg = response.getString("error_msg");
                    Toast.makeText(getApplicationContext(),
                            errorMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                // JSON error
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

        };

        Response.ErrorListener errorListener = (error) -> {
            Toast.makeText(getApplicationContext(),
                    "Error with login", Toast.LENGTH_LONG).show();
            {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Volley error" +
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        };

        UserLoginController.login(nickname, password, listener, errorListener);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}
