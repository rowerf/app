package com.example.programmeerproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences prefs = null;
    Button btn_login, btn_register;
    EditText et_username, et_password;
    String str_username, str_password;
    UserDBHandler handler;
    Integer user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*// SharedPreferences*/
        prefs = getSharedPreferences("name", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);


        // If isLoggenIn is true, then the user is automatically logged in and send to the next
        // activity, namely MapsActivity.java
        if (isLoggedIn) {
            handler = new UserDBHandler(LoginActivity.this);
            Intent i = new Intent(LoginActivity.this, MapsActivity.class);

            String username = prefs.getString("username", null);
            user_id = handler.getUserId(username);
            // Transfer user_id to next activity
            i.putExtra("user_id", user_id);
            startActivity(i);
        } setContentView(R.layout.activity_login);

        // Initialize buttons
        btn_login = findViewById(R.id.login);
        btn_register = findViewById(R.id.register);
        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);

        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            Intent i_intent = new Intent(getApplicationContext(), InstructionsActivity.class);
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).apply();
            startActivity(i_intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                // Extract username and password strings
                str_username = et_username.getText().toString().trim();
                str_password = et_password.getText().toString().trim();

                // if email and or password is empty, notify user
                TextView tv_fb_username = findViewById(R.id.fb_username);
                TextView tv_fb_password = findViewById(R.id.fb_password);
                TextView tv_fb_failed = findViewById(R.id.fb_failed);

                // Set views to gone (for when btn_login is clicked again)
                tv_fb_username.setVisibility(View.GONE);
                tv_fb_password.setVisibility(View.GONE);
                tv_fb_failed.setVisibility(View.GONE);

                // Notify user when no username is entered
                if (str_username.isEmpty()) {
                    tv_fb_username.setVisibility(View.VISIBLE);
                }

                // Notify user no password is entered
                if (str_password.isEmpty()){
                    tv_fb_password.setVisibility(View.VISIBLE);
                }

                // If username and password are filled in, save the new user and go to MapsActivity
                else {
                    handler = new UserDBHandler(LoginActivity.this);
                    // Check if user doesn't already exist
                    if (handler.checkUser(str_username, str_password)) {
                        Intent i = new Intent(LoginActivity.this, MapsActivity.class);

                        // Save login to SharedPreferences
                        SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                        editor.putString("username", str_username);
                        editor.putString("password", str_password);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                        // Transfer user_id to next activity (MapsActivity.java)
                        user_id = handler.getUserId(str_username);
                        i.putExtra("user_id", user_id);
                        startActivity(i);
                    } else {
                        // When only a password is entered, don't give the feedback
                        if (tv_fb_username.getVisibility() != View.VISIBLE)
                        tv_fb_failed.setVisibility(View.VISIBLE);
                    }
                }
                break;

            // When the user is new and wants to register, start RegisterActivity.java
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    }
}
