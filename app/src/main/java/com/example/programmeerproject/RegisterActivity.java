package com.example.programmeerproject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class RegisterActivity extends AppCompatActivity {

    EditText ETUsername;
    EditText ETEmail;
    EditText ETPassword;

    TextView FBUsername;
    TextView FBEmail;
    TextView FBPassword;

    String username;
    String email;
    String password;

    String preferences;

    UserDBHandler handler;
    User user;

    Button confirm;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ETUsername = findViewById(R.id.username);
        ETEmail = findViewById(R.id.email);
        ETPassword = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* When confirm is clicked */

                /* Extract username and check if not already in user db */
                // get: username, password and preferences

                FBUsername = findViewById(R.id.FBusername);
                FBEmail = findViewById(R.id.FBemail);
                FBPassword = findViewById(R.id.FBpassword);

                username = ETUsername.getText().toString();
                email = ETEmail.getText().toString();
                password = ETPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()){
                    //Toast.makeText(RegisterActivity.this, username, Toast.LENGTH_LONG).show();
                    // UpdateUI with notification that username is empty

                    FBUsername.setText("Please enter a username");
                    FBUsername.setTextColor(getResources().getColor(R.color.colorPrimary));

                    FBEmail.setText("Please enter an e-mail adress");
                    FBEmail.setTextColor(getResources().getColor(R.color.colorPrimary));

                    FBPassword.setText("Please enter a password");
                    FBPassword.setTextColor(getResources().getColor(R.color.colorPrimary));

                    if (password.length() <6 && !password.isEmpty()){
                        // The password should be at least 5 characters long
                        FBPassword.setText("Please enter at least 5 characters");
                    }
                }

                // If no checkboxes are checked, ask user if sure?

                // Extract the preferences from check boxes
                // Initiate all checkboxes
                CheckBox vegetarian = findViewById(R.id.vegetarian);
                CheckBox biological = findViewById(R.id.biological);
                CheckBox glutenfree = findViewById(R.id.glutenfree);
                CheckBox lactosefree = findViewById(R.id.lactosefree);
                CheckBox sugarfree = findViewById(R.id.sugarfree);

                preferences = "";
                if (vegetarian.isChecked()){
                    preferences += "vegetarian ";
                }
                if (biological.isChecked()){
                    preferences += "biological ";
                }
                if (glutenfree.isChecked()){
                    preferences += "glutenfree ";
                }
                if (lactosefree.isChecked()){
                    preferences += "lactose-free ";
                }
                if (sugarfree.isChecked()){
                    preferences += "sugar-free ";
                }
                //Toast.makeText(RegisterActivity.this, preferences, Toast.LENGTH_LONG).show();

                // If the username is valid and not yet in the User DB, add user
                handler = new UserDBHandler(RegisterActivity.this);
                user = new User();
                
                postDataToSQLite();

                //User user = new User(username, preferences);
                //dbHandler.addHandler(user);
                // Go to the maps activity
                //startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* When cancel is clicked */
                // Return to FirstActivity
                startActivity(new Intent(RegisterActivity.this, FirstActivity.class));
            }
        });

    }

    private void postDataToSQLite() {
        if (!handler.checkUser(email)) {
            user.setUsername(username.trim());
            user.setEmail(email.trim());
            user.setPassword(password);
            user.setPreferences(preferences);

            handler.addUser(user);
            //startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
            Intent i = new Intent(RegisterActivity.this, MapsActivity.class);
            i.putExtra("user", user);
            startActivity(i);
            //startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
        } else {
            Toast.makeText(RegisterActivity.this, username + " " + email + " " + password + " " + preferences, Toast.LENGTH_LONG).show();
        }
    }


}
