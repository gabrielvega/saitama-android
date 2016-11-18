package com.saitama.rentbikes.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gabrielvega on 2016-11-17.
 */

public class PlacesResponse implements Parcelable {

    @SerializedName("results")
    ArrayList<Place> places;

    public ArrayList<Place> getPlaces() {
        return places;
    }

    public void setPlaces(ArrayList<Place> places) {
        this.places = places;
    }

    protected PlacesResponse(Parcel in) {
        places = in.createTypedArrayList(Place.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(places);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlacesResponse> CREATOR = new Creator<PlacesResponse>() {
        @Override
        public PlacesResponse createFromParcel(Parcel in) {
            return new PlacesResponse(in);
        }

        @Override
        public PlacesResponse[] newArray(int size) {
            return new PlacesResponse[size];
        }
    };
}
