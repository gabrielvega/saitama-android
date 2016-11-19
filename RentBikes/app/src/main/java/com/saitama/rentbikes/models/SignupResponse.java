package com.saitama.rentbikes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author gabrielvega
 * @version 1.0.0
 *          created 2016-11-17
 * @since 1.0.0
 */

public class SignupResponse implements Parcelable {

    private String accessToken;

    public SignupResponse() {

    }

    protected SignupResponse(Parcel in) {
        accessToken = in.readString();
    }

    public static final Creator<SignupResponse> CREATOR = new Creator<SignupResponse>() {
        @Override
        public SignupResponse createFromParcel(Parcel in) {
            return new SignupResponse(in);
        }

        @Override
        public SignupResponse[] newArray(int size) {
            return new SignupResponse[size];
        }
    };

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(accessToken);
    }
}
