package com.example.programmeerproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    // things
    EditText email;
    EditText password;

    UserDBHandler handler;

    Button login;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        // initialize buttons
        login = findViewById(R.id.login);
        register  = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            email = findViewById(R.id.email);
            password = findViewById(R.id.password);

            String str_email = email.getText().toString().trim();
            String str_password = password.getText().toString().trim();

            // if email and or password is empty, notify user
            TextView FBemail = findViewById(R.id.FBemail);
            TextView FBpassword = findViewById(R.id.FBpassword);

            if (str_email.isEmpty()){
                FBemail.setText("Please enter an e-mail address");
                FBemail.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
            if (str_password.isEmpty()){
                FBpassword.setText("Please enter a password");
                FBpassword.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else {
                handler = new UserDBHandler(FirstActivity.this);
                if (handler.checkUser(str_email, str_password)) {
                    Intent i = new Intent(FirstActivity.this, MapsActivity.class);

                    i.putExtra("email", str_email);
                    startActivity(i);
                } else {
                    Toast.makeText(FirstActivity.this, "invalid", Toast.LENGTH_LONG).show();
                }
            }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /* In case the user is new and wants to register */
            startActivity(new Intent(FirstActivity.this, RegisterActivity.class));
            }
        });


    }

}
