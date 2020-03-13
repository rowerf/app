package com.example.programmeerproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import static android.view.View.GONE;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_confirm, btn_cancel;
    EditText et_username, et_password;
    TextView tv_fb_username, tv_fb_username_2, tv_fb_password, tv_fb_password_2, tv_fb_preferences;
    String str_username, str_password, str_preferences;
    boolean atLeastOneChecked;
    UserDBHandler handler;
    User user;
    Integer user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initiate views
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        btn_confirm = findViewById(R.id.confirm);
        btn_cancel = findViewById(R.id.cancel);

        btn_confirm.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                // Initiate TextViews, CheckBoxes and other variables
                tv_fb_username = findViewById(R.id.fb_username);
                tv_fb_username_2 = findViewById(R.id.fb_username2);
                tv_fb_password = findViewById(R.id.fb_password);
                tv_fb_password_2 = findViewById(R.id.fb_password2);
                tv_fb_preferences = findViewById(R.id.fb_preferences);

                CheckBox vegetarian = findViewById(R.id.vegetarian);
                CheckBox vegan = findViewById(R.id.vegan);
                CheckBox biological = findViewById(R.id.biological);
                CheckBox glutenfree = findViewById(R.id.glutenfree);
                CheckBox lactosefree = findViewById(R.id.lactosefree);
                CheckBox sugarfree = findViewById(R.id.sugarfree);

                atLeastOneChecked = false;
                str_preferences = "";

                // Set textviews to gone, for when btn_confirn is clicked again
                tv_fb_username.setVisibility(GONE);
                tv_fb_username_2.setVisibility(GONE);
                tv_fb_password.setVisibility(GONE);
                tv_fb_password_2.setVisibility(GONE);
                tv_fb_preferences.setVisibility(GONE);

                // Extract username and password
                str_username = et_username.getText().toString();
                str_password = et_password.getText().toString();

                // Check EditTexts and give feedback where necessary
                if (str_username.isEmpty()){
                    // UpdateUI with notification that username is empty
                    tv_fb_username.setVisibility(View.VISIBLE);
                }
                if (str_password.isEmpty()){
                    // Notify the user that password is empty
                    tv_fb_password.setVisibility(View.VISIBLE);
                }
                if (str_password.length() <5 && !str_password.isEmpty()){
                    // Notify the user when username is less than 5 characters long
                    tv_fb_password_2.setVisibility(View.VISIBLE);
                }

                // Notify user if no preferences are given
                if (vegetarian.isChecked() || vegan.isChecked() ||  biological.isChecked() ||
                        glutenfree.isChecked() || lactosefree.isChecked() || sugarfree.isChecked()){
                    atLeastOneChecked = true;
                }

                // Check if at least one checkbox is checked
                if (!atLeastOneChecked) {
                    tv_fb_preferences.setVisibility(View.VISIBLE);
                }
                else {
                    // Save preferences as string
                    if (vegetarian.isChecked()){
                        str_preferences += "vegetarian,";
                    }
                    if (vegan.isChecked()){
                        str_preferences += "vegan,";
                    }
                    if (biological.isChecked()){
                        str_preferences += "biological,";
                    }
                    if (glutenfree.isChecked()){
                        str_preferences += "glutenfree,";
                    }
                    if (lactosefree.isChecked()){
                        str_preferences += "lactose-free,";
                    }
                    if (sugarfree.isChecked()){
                        str_preferences += "sugar-free,";
                    }

                    // Trim preferences so that the last character is 'removed'
                    if (str_preferences != null && str_preferences.length() > 0
                            && str_preferences.charAt(str_preferences.length()-1) == ','){
                        str_preferences = str_preferences.substring(0, str_preferences.length()-1);
                    }

                    // Initiate UserDBHandler and post data to the database.
                    handler = new UserDBHandler(RegisterActivity.this);

                    // Post new user to DB and go to MapsActivity.java
                    postDataToSQLite();
                }
                break;

            case R.id.cancel:
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        }
    }

    private void postDataToSQLite() {
        if (!handler.checkUser(str_username)) {
            // Create a new user instance
            user = new User();
            user.setUsername(str_username.trim());
            user.setPassword(str_password);
            user.setPreferences(str_preferences);
            handler.addUser(user);

            // Save user data to shared preferences
            SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
            editor.putString("username", str_username);
            editor.putString("password", str_password);
            editor.putBoolean("isLoggedIn", true);
            editor.apply();

            Intent i = new Intent(RegisterActivity.this, MapsActivity.class);
            user_id = handler.getUserId(str_username);
            i.putExtra("user_id", user_id);
            startActivity(i);
        } else {
            // UpdateUI that username already exists
            if (!str_username.isEmpty()){
                tv_fb_username_2.setVisibility(View.VISIBLE);
            }
        }
    }
}
