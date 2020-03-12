package com.example.programmeerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity implements View.OnClickListener {

    Integer user_id;
    UserDBHandler handler;
    User user;
    TextView tv_fb_preferences;
    TextView tv_fb_username_2;
    CheckBox vegetarian, vegan, biological, glutenfree, lactosefree, sugarfree;
    EditText et_username;
    String str_username;
    String str_get_et_username;
    String updated_preferences;
    boolean atLeastOneChecked;
    UserDBHandler handler2;
    Button btnCancel, btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // Initiate views and fill them with username, and preferences
        et_username = findViewById(R.id.et_username);
        tv_fb_username_2 = findViewById(R.id.fb_username2);
        tv_fb_preferences = findViewById(R.id.fb_preferences);

        vegetarian = findViewById(R.id.vegetarian);
        vegan = findViewById(R.id.vegan);
        biological = findViewById(R.id.biological);
        glutenfree = findViewById(R.id.glutenfree);
        lactosefree = findViewById(R.id.lactosefree);
        sugarfree = findViewById(R.id.sugarfree);

        // This boolean is used to check whether at least one preference is selected
        atLeastOneChecked = false;

        // Import user based on user_id from previous intent -- copied from MapsActivty
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id", 100);
        handler = new UserDBHandler(PreferencesActivity.this);
        user = handler.getUser(user_id);

        // Retrieve username and populate EditText with said username
        str_username = user.getUsername();
        et_username.setText(str_username);

        // Get user's preferences and convert to a list to populate the checkboxes
        String str_preferences = user.getPreferences();
        String[] lst_preferences = str_preferences.split(",");
        List<String> list = new ArrayList<>();
        Collections.addAll(list, lst_preferences);

        // Use the list to populate the checkboxes
        for(String s : list){
            if (s.equals("vegetarian")){
                vegetarian.setChecked(true);
            }
            if (s.equals("vegan")){
                vegan.setChecked(true);
            }
            if (s.equals("biological")){
                biological.setChecked(true);
            }
            if (s.equals("glutenfree")){
                glutenfree.setChecked(true);
            }
            if (s.equals("lactose-free")){
                lactosefree.setChecked(true);
            }
            if (s.equals("sugar-free")){
                sugarfree.setChecked(true);
            }
        }

        // Initiate buttons and set onclicklisteners
        btnCancel = findViewById(R.id.cancel);
        btnSave = findViewById(R.id.save);

        // Set clicklisteners on cancel and save button
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            case R.id.save:
                // Most of the code underneath is also used in RegisterActivity.java
                // Make sure a username is given
                str_get_et_username = et_username.getText().toString();

                if(str_get_et_username.isEmpty()){
                    TextView tv_fb_username = findViewById(R.id.fb_username);
                    tv_fb_username.setVisibility(View.VISIBLE);
                }

                // When no checkboxes are checked, ask user if sure
                updated_preferences = "";

                if (vegetarian.isChecked() || vegan.isChecked() ||  biological.isChecked() ||
                        glutenfree.isChecked() || lactosefree.isChecked() || sugarfree.isChecked()){
                    atLeastOneChecked = true;
                }
                if (!atLeastOneChecked){
                    tv_fb_preferences.setVisibility(View.VISIBLE);
                }
                else {
                    // Save preferences as a string
                    if (vegetarian.isChecked()){
                        updated_preferences += "vegetarian,";
                    }
                    if (vegan.isChecked()){
                        updated_preferences += "vegan,";
                    }
                    if (biological.isChecked()){
                        updated_preferences += "biological,";
                    }
                    if (glutenfree.isChecked()){
                        updated_preferences += "glutenfree,";
                    }
                    if (lactosefree.isChecked()){
                        updated_preferences += "lactose-free,";
                    }
                    if (sugarfree.isChecked()){
                        updated_preferences += "sugar-free,";
                    }
                    // Trim preferences so that the last character (a comma) is removed
                    if (updated_preferences != null && updated_preferences.length() > 0
                            && updated_preferences.charAt(updated_preferences.length()-1) == ','){
                        updated_preferences = updated_preferences.substring(0, updated_preferences.length()-1);
                }

                // Initiate handler
                handler2 = new UserDBHandler(PreferencesActivity.this);

                // Change the User instance with setters and post to DB Handler
                updateDataToSQLite();
            }
        }
    }

    private void updateDataToSQLite() {
        if (!handler2.checkUser(str_get_et_username) || str_get_et_username.equals(str_username)){
            // Update user data based on retrieved data
            handler2.updateUser(user.getId(), str_get_et_username, updated_preferences);

            // Start intent to MapsActivity.java
            Intent i = new Intent(PreferencesActivity.this, MapsActivity.class);
            user_id = handler2.getUserId(str_get_et_username);
            i.putExtra("user_id", user_id);
            startActivity(i);
        } else {
            // UpdateUI that username already exists
            if (!str_get_et_username.isEmpty()) {
                tv_fb_username_2.setVisibility(View.VISIBLE);
            }
        }
    }
}
