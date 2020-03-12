package com.example.programmeerproject;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VenuesRequest implements Response.Listener<JSONObject>, Response.ErrorListener {

    public Context context;
    public Callback cb;
    ArrayList<Venue> al_venues;

    public interface Callback{
        void gotVenues(ArrayList<Venue> venues);
        void gotError(String message);
    }

    // Constructor
    VenuesRequest(Context c) { context = c;}

    public void getVenues(Callback activity, String url){
        RequestQueue queue = Volley.newRequestQueue((Context) activity);
        JsonObjectRequest request_venues = new JsonObjectRequest(url, null, this, this);
        queue.add(request_venues);

        // Call activity
        cb = activity;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        cb.gotError(context.getString(R.string.error_message));
    }

    @Override
    public void onResponse(JSONObject response) {
        al_venues = new ArrayList<>();
        try {
            JSONArray results = response.getJSONArray("results");
            for (int i=0;i<5;i++){
                JSONObject venue = results.getJSONObject(i);

                // Make a Venue instance to save data to and add to lst_venues
                Venue v = new Venue();

                // The to be extracted values
                int id = venue.getInt("id");
                String name = venue.getString("name");
                String category = venue.getString("category");
                String telephone = venue.getString("telephone");
                int distance = venue.getInt("distance");

                // Extract from Object: address, geolocation
                JSONObject address = venue.getJSONObject("address");
                String street = address.getString("street");
                String zipcode = address.getString("zipcode");
                String city = address.getString("city");

                JSONObject geolocation = venue.getJSONObject("geolocation");
                Double latitude = geolocation.getDouble("latitude");
                Double longitude = geolocation.getDouble("longitude");

                // Set venue details and add to the arraylist of venues
                v.set_venue_id(id);
                v.set_venue_name(name);
                v.setCategory(category);
                v.setTelephone(telephone);
                v.setStreet(street);
                v.setZipcode(zipcode);
                v.setCity(city);

                v.setLatitude(Float.parseFloat(String.valueOf(latitude)));
                v.setLongitude(Float.parseFloat(String.valueOf(longitude)));
                v.setDistance(distance);

                al_venues.add(v);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Callback to activity (Maps) and 'give' the ArrayList with venues
        cb.gotVenues(al_venues);
    }
}
