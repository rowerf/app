package com.example.programmeerproject;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources.NotFoundException;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.String.valueOf;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        LocationListener, VenuesRequest.Callback, View.OnClickListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    Button btn_logout, btn_preferences;
    TextView info, info2;
    String str_latitude, str_longitude;
    Integer user_id;

    GoogleApiClient google_api_client;
    Location last_location;
    Marker current_location_marker;
    LocationRequest location_request;
    GoogleMap map;

    UserDBHandler handler;
    User user;
    VenueAdapter adapter;

    LatLngBounds bounds;
    LatLngBounds.Builder builder;

    boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Get user_id from intent
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id",100);

        // Use user_id to get user
        handler = new UserDBHandler(MapsActivity.this);
        user = handler.getUser(user_id);

        // show the user which prefernces are included
        info = findViewById(R.id.info);
        info2 = findViewById(R.id.info2);

        info.setText("Bezig met zoeken...");
        info.setVisibility(View.VISIBLE);

        // set false
        doubleBackToExitPressedOnce = false;

        // Check if the application has permission to use location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        // Make sure there is a fragment, if so: get the Map (which is an asynchronous task)
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initiate buttons
        btn_logout = findViewById(R.id.btn_logout);
        btn_preferences = findViewById(R.id.btn_preferences);

        // set clickListeners on the above mentioned buttons
        btn_logout.setOnClickListener(this);
        btn_preferences.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                // Empty saved name/value pairs and boolean
                SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
                editor.putString("username", "");
                editor.putString("password", "");
                editor.putBoolean("isLoggedIn", false);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("finish", true);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
                break;
            case R.id.btn_preferences:
                // start new activity to Preferences and give with: the user_id
                Intent i = new Intent(getApplicationContext(), PreferencesActivity.class);
                i.putExtra("user_id", user.getId());
                startActivity(i);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Initiate map and set some controls
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);

        // Initialize google play services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                map.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            map.setMyLocationEnabled(true);
        }

        // apply style to map
        try{
            boolean succes = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this,R.raw.style_json));
            if (!succes){
                // Unable to apply the style
                Log.d("unable to apply style", getString(R.string.unable_apply_style));
            }
        } catch (NotFoundException e) {
            // toast cant find style
            Log.d("style not found", "style could not be found.");
        }
    }

    protected synchronized void buildGoogleApiClient() {
        // conntect to MapsAPI and add callbacks and listeners
        google_api_client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        google_api_client.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        location_request = new LocationRequest();
        location_request.setInterval(1000);
        location_request.setFastestInterval(1000);
        location_request.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(google_api_client,
                    location_request, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
        else {
            // location is not turned on
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        last_location = location;
        if (current_location_marker != null) {
            current_location_marker.remove();
        }

        // Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Based on provider, extract the longitude and latitude to make and add markers on the map
        assert provider != null;
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();

            str_latitude = String.valueOf(latitude);
            str_longitude = String.valueOf(longitude);
            // Initiate builder to add the current location
            builder = new LatLngBounds.Builder();

            // Set a marker on map on user's location
             Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    markerOptions.title("" + latLng + "," + state + "," + country);
                    map.addMarker(markerOptions);
                    // Add 'marker' to include it in the bounds and make it visible on the map
                    builder.include(new LatLng(latitude, longitude));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (google_api_client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(google_api_client,
                    this);
        }

        // When lat and lon are retrieved, start request to API
        // Put preferences in a API request
        String url = "https://api.eet.nu/venues?tags="+user.getPreferences()+"&geolocation="
                + str_latitude +","+ str_longitude;

        // Make request
        VenuesRequest venuesRequest = new VenuesRequest(this);
        venuesRequest.getVenues(this, url);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    if (google_api_client == null) {
                        buildGoogleApiClient();
                    }
                    map.setMyLocationEnabled(true);
                }
            } else {
                info2.setText(R.string.location_permission);
                info2.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Check whether the back button is pressed again
        if(doubleBackToExitPressedOnce){
            // Do something with saved instances
            SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
            editor.putString("username", "");
            editor.putString("password", "");
            editor.putBoolean("isLoggedIn", false);
            editor.apply();

            // Go to LoginActivity.java
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.putExtra("finish", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Klik nog een keer om uit te loggen", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        },2000);
    }

    @Override
    public void gotVenues(ArrayList<Venue> venues) {
        // Hide UI to notify user a search was succesful
        info.setVisibility(View.GONE);

        // Initiate adapter for the venues
        adapter = new VenueAdapter(MapsActivity.this, venues);

        // "Grab" ListView and fill rows via a custom adapter
        final ListView lv_venues = findViewById(R.id.lv_venues);
        lv_venues.setAdapter(adapter);

        // Extract phone numbers and put
        final ArrayList<String> strA_telephones = new ArrayList<>();

        // Do something here to display the markers on map
        for (int i = 0; i < venues.size();i++){
            Venue venue = venues.get(i);
            double latitude = venue.getLatitude();
            double longitude = venue.getLongitude();

            // Add the telephone number to the string array
            strA_telephones.add(venue.getTelephone());

            // Add marker to builder and show on map
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            markerOptions.position(new LatLng(latitude, longitude)).title(venue.get_venue_name());
            map.addMarker(markerOptions);
            builder.include(markerOptions.getPosition());
        }

        lv_venues.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Extract phone number and open dialer with clicked venue's telephone number
                String str_telephone = strA_telephones.get(position);
                Intent phone_intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",
                        str_telephone,null));
                startActivity(phone_intent);
            }
        });

        // Convert builder to bounds and use to update the camera so that all venues are visible
        bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 20);
        map.animateCamera(cu);
    }

    @Override
    public void gotError(String message) {
        Toast.makeText(MapsActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}

