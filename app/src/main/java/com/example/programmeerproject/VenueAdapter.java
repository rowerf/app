package com.example.programmeerproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class VenueAdapter extends ArrayAdapter<Venue> {

    private Context mContext;
    private List<Venue> venuesList = new ArrayList<>();

    public VenueAdapter(@NonNull Context context, ArrayList<Venue> list) {
        super(context, 0 , list);
        mContext = context;
        venuesList = list;
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Venue currentVenue = venuesList.get(position);

        // find views
        TextView name = listItemView.findViewById(R.id.name);
        TextView category = listItemView.findViewById(R.id.category);
        TextView street = listItemView.findViewById(R.id.street);
        TextView zipcode = listItemView.findViewById(R.id.zipcode);
        TextView city = listItemView.findViewById(R.id.city);
        TextView telephone = listItemView.findViewById(R.id.telephone);
        TextView distance = listItemView.findViewById(R.id.distance);
        // Set TextViews
        name.setText(currentVenue.get_venue_name());
        category.setText(String.format("keuken: %s", currentVenue.getCategory()));
        street.setText(currentVenue.getStreet());
        zipcode.setText(currentVenue.getZipcode()+ " " + currentVenue.getCity());
        //city.setText(currentVenue.getCity());
        telephone.setText(currentVenue.getTelephone());
        distance.setText(String.format("afstand: %s m", (String.valueOf(currentVenue.getDistance()))));

        // TODO: find out how to place markers on map (in MapsActivity)

        return listItemView;
    }
}