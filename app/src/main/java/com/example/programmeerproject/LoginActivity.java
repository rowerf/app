package com.example.programmeerproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText et_username;
    EditText et_password;
    UserDBHandler handler;
    Button login;
    Button register;
    Integer user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // SharedPreferences
        SharedPreferences prefs = getSharedPreferences("name", MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);

        if(isLoggedIn){
            handler = new UserDBHandler(LoginActivity.this);
            Intent i = new Intent(LoginActivity.this, MapsActivity.class);
            // Transfer user_id to next activity
            String username = prefs.getString("username", null);
            user_id = handler.getUserId(username);
            i.putExtra("user_id", user_id);
            startActivity(i);
        }
        setContentView(R.layout.activity_login);

        // Initialize buttons
        login = findViewById(R.id.login);
        register  = findViewById(R.id.register);
        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);

        // When the login button is clicked
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            // Extract username and password strings
            String str_username = et_username.getText().toString().trim();
            String str_password = et_password.getText().toString().trim();

            // if email and or password is empty, notify user
            TextView fb_email = findViewById(R.id.fb_username);
            TextView fb_password = findViewById(R.id.fb_password);
            TextView fb_failed = findViewById(R.id.fb_failed);

            if (str_username.isEmpty()){
                // Notify user no username is entered
                fb_email.setVisibility(View.VISIBLE);
            }
            if (str_password.isEmpty()){
                // Notify user no password is entered
                fb_password.setVisibility(View.VISIBLE);
            } else {
                // If a username and password are filled in
                handler = new UserDBHandler(LoginActivity.this);
                if (handler.checkUser(str_username, str_password)) {
                    Intent i = new Intent(LoginActivity.this, MapsActivity.class);

                    // Save login to SharedPreferences
                    SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();

                    editor.putString("username", str_username);
                    editor.putString("password", str_password);
                    editor.putBoolean("isLoggedIn", true);
                    editor.apply();


                    // Transfer user_id to next activity
                    user_id = handler.getUserId(str_username);
                    i.putExtra("user_id", user_id);
                    startActivity(i);
                } else {
                    fb_failed.setVisibility(View.VISIBLE);
                }
            }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* In case the user is new and wants to register */
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

}
