package com.example.programmeerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    String lat;
    String lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        lat = intent.getExtras().getString("lat");
        lon = intent.getExtras().getString("lon");

        //Toast.makeText(SearchActivity.this, lat + " " + lon, )
        Toast.makeText(SearchActivity.this, lat + ", " + lon, Toast.LENGTH_LONG).show();

        new RequestTask().execute("https://api.eet.nu/venues?geolocation="+lat+"%2C"+lon);
    }
}
