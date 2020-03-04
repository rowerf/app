package com.example.programmeerproject;

/* TO DO
*  When location is not turned on, and permission is given: notify user to turn location on
* Fix: the errors that occur when 'back'-button is pressed
* Come up with a strategy for device rotation
* */

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
import android.graphics.Camera;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.lang.String.valueOf;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        LocationListener, VenuesRequest.Callback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    GoogleMap mMap;
    String strLongitude; String strLatitude;
    Integer user_id;
    UserDBHandler handler;
    TextView info;
    User user;
    VenueAdapter mAdapter;
    LatLngBounds bounds;
    LatLngBounds.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        String test = String.valueOf(R.string.welcome_message);

        // Get user_id from intent
        Intent intent = getIntent();
        user_id = intent.getIntExtra("user_id",100);

        // Use user_id to call getUser() which returns a user instance
        handler = new UserDBHandler(MapsActivity.this);
        user = handler.getUser(user_id);

        // Button stuff
        //logout = findViewById(R.id.log);

        // some sort of check whether data can be extracted from user
        info = findViewById(R.id.info);
        info.setText(user.getPreferences());
        info.setVisibility(View.VISIBLE);

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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);

        // TODO: find out why the zoom only works when this seemingly useless line is here
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        // Initialize google play services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
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
            Log.d("style not found", "");
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

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
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        // Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();

            strLatitude = String.valueOf(latitude);
            strLongitude = String.valueOf(longitude);

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
                    builder.include(new LatLng(latitude, longitude));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        //mCurrLocationMarker = mMap.addMarker(markerOptions);

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }

        // When lat and lon are retrieved, start request to API
        // Put preferences in a API request
        String url = "https://api.eet.nu/venues?tags="+user.getPreferences()+"&geolocation="
                +strLatitude+","+strLongitude;

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
                    if (mGoogleApiClient == null) {
                        buildGoogleApiClient();
                    }
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "permission denied",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public void gotVenues(ArrayList<Venue> venues) {
        info = findViewById(R.id.info);
        //info.setText(venues.toString());
        //info.setVisibility(View.VISIBLE);

        //ArrayAdapter<Object> adapter = new ArrayAdapter<>(
                //this, android.R.layout.simple_list_item_1, venues);
        mAdapter = new VenueAdapter(MapsActivity.this, venues);
        // "Grab" ListView and fill rows via a custom adapter
        ListView lv_venues = findViewById(R.id.lv_venues);
        lv_venues.setAdapter(mAdapter);

        builder = new LatLngBounds.Builder();

        // Do something here to display the markers on map
        for (int i = 0; i < venues.size();i++){
            Venue venue = venues.get(i);
            double latitude = venue.getLatitude();
            double longitude = venue.getLongitude();
            //Toast.makeText(MapsActivity.this, String.valueOf(latitude), Toast.LENGTH_SHORT).show();

            // Add marker to builder and show on map
            MarkerOptions markerOptions = new MarkerOptions();


            markerOptions.position(new LatLng(latitude, longitude)).title(venue.get_venue_name());
            mMap.addMarker(markerOptions);
            builder.include(markerOptions.getPosition());
            //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(venue.get_venue_name()));


        }

        // CameraUpdate so that all markers are visible

        bounds = builder.build();
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 11));
        /*CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 11);
        mMap.animateCamera(cu);*/
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 4));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        /*int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = getResources().getDimensionPixelSize(R.dimen.fab_margin);*/
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 40);
        mMap.animateCamera(cu);
    }

    @Override
    public void gotError(String message) {
        Toast.makeText(MapsActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void btnLogout(View view) {
        SharedPreferences.Editor editor = getSharedPreferences("name", MODE_PRIVATE).edit();
        editor.putString("username", "");
        editor.putString("password","");
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("finish", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }

    public void btnPreferences(View view) {
    }
}

