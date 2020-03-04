package com.example.programmeerproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    Button confirm, cancel;
    EditText et_username, et_password;
    TextView fb_username, fb_username_2, fb_password, fb_password_2, fb_preferences;
    String username, password;
    //List<String> preferences;
    String preferences;
    boolean atLeastOneChecked;
    //StringBuilder csvPreferences;
    UserDBHandler handler;
    UserDBHandler handler2;
    User user;
    Integer user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initiate views
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);

        /* When confirm is clicked */
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initiate things
                fb_username = findViewById(R.id.fb_username);
                fb_username_2 = findViewById(R.id.fb_username2);
                fb_password = findViewById(R.id.fb_password);
                fb_password_2 = findViewById(R.id.fb_password2);
                fb_preferences = findViewById(R.id.fb_preferences);

                CheckBox vegetarian = findViewById(R.id.vegetarian);
                CheckBox vegan = findViewById(R.id.vegan);
                CheckBox biological = findViewById(R.id.biological);
                CheckBox glutenfree = findViewById(R.id.glutenfree);
                CheckBox lactosefree = findViewById(R.id.lactosefree);
                CheckBox sugarfree = findViewById(R.id.sugarfree);

                atLeastOneChecked = false;
                //preferences = new ArrayList<>();
                preferences = "";

                // Extract username and password
                username = et_username.getText().toString();
                password = et_password.getText().toString();

                // Check EditTexts and give feedback when necessary
                if (username.isEmpty()){
                    // UpdateUI with notification that username is empty
                    fb_username.setVisibility(View.VISIBLE);
                }
                if (password.isEmpty()){
                    // Notify the user that password is empty
                    fb_password.setVisibility(View.VISIBLE);
                }
                if (password.length() <5 && !password.isEmpty()){
                    // Notify the user when username is less than 5 characters long
                    fb_password_2.setVisibility(View.VISIBLE);
                }

                // Notify user if no preferences are given
                if (vegetarian.isChecked() || vegan.isChecked() ||  biological.isChecked() ||
                        glutenfree.isChecked() || lactosefree.isChecked() || sugarfree.isChecked()){
                    atLeastOneChecked = true;
                }

                // When at least one preference is given, convert it into comma separated value list
                //csvPreferences = new StringBuilder();
                //for(String s : preferences){
                    //csvPreferences.append(s);
                    //csvPreferences.append(",");
                //}

                // Check if at least one checkbox is checked
                if (!atLeastOneChecked) {
                    fb_preferences.setVisibility(View.VISIBLE);
                }
                else {
                    //TODO: When someone indicates not to have any preference

                    // Save preferences as string
                    if (vegetarian.isChecked()){
                    //preferences.add("vegetarian");
                    preferences += "vegetarian,";
                    }
                    if (vegan.isChecked()){
                        //preferences.add("vegan");
                        preferences += "vegan,";
                    }
                    if (biological.isChecked()){
                    //preferences.add("biological");
                        preferences += "biological,";
                    }
                    if (glutenfree.isChecked()){
                    //preferences.add("glutenfree");
                        preferences += "glutenfree,";
                    }
                    if (lactosefree.isChecked()){
                    //preferences.add("lactose-free");
                        preferences += "lastose-free,";
                    }
                    if (sugarfree.isChecked()){
                    //preferences.add("sugar-free");
                        preferences += "sugar-free,";
                    }

                    // Trim preferences so that the last character is 'removed'
                    if (preferences != null && preferences.length() > 0
                            && preferences.charAt(preferences.length()-1) == ','){
                        preferences = preferences.substring(0, preferences.length()-1);
                    }

                    // Initiate UserDBHandler and user
                    handler = new UserDBHandler(RegisterActivity.this);

                    // Post new user to DB and go to MapsActivity.java
                    postDataToSQLite();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Return to LoginActivity.java when cancel is clicked */
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void postDataToSQLite() {
        if (!handler.checkUser(username)) {
            user = new User();
            user.setUsername(username.trim());
            user.setPassword(password);
            user.setPreferences(preferences);
            handler.addUser(user);

            //Toast.makeText(RegisterActivity.this, user.getPreferences(), Toast.LENGTH_LONG).show();

            Intent i = new Intent(RegisterActivity.this, MapsActivity.class);
            //user_id = user.getId();

            handler2 = new UserDBHandler(RegisterActivity.this);
            user_id = handler.getUserId(username);
            i.putExtra("user_id", user_id);
            //Toast.makeText(RegisterActivity.this, String.valueOf(user_id), Toast.LENGTH_LONG).show();
            startActivity(i);
        } else {
            // UpdateUI that username already exists
            if (!username.isEmpty()){
                fb_username_2.setVisibility(View.VISIBLE);
            }
        }
    }
}
