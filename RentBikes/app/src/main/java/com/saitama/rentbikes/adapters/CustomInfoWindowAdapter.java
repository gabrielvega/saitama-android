package com.saitama.rentbikes.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.saitama.rentbikes.R;
import com.saitama.rentbikes.activities.RentBicycleActivity;

/**
 * Created by gabrielvega on 2016-11-17.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    LayoutInflater inflater = null;
    Context ctx;

    public CustomInfoWindowAdapter(Context ctx, LayoutInflater inflater) {
        this.inflater = inflater;
        this.ctx = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View popup = inflater.inflate(R.layout.custom_info_window, null);

        TextView tv = (TextView) popup.findViewById(R.id.title);
        tv.setText(marker.getTitle());

        return (popup);
    }
}
