package com.saitama.rentbikes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author gabrielvega
 * @since 1.0.0
 * @version 1.0.0
 * created 2016-11-17
 */

public class RentResponse implements Parcelable {

    private String message;

    protected RentResponse(Parcel in) {
        message = in.readString();
    }

    public static final Creator<RentResponse> CREATOR = new Creator<RentResponse>() {
        @Override
        public RentResponse createFromParcel(Parcel in) {
            return new RentResponse(in);
        }

        @Override
        public RentResponse[] newArray(int size) {
            return new RentResponse[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(message);
    }
}
