package com.saitama.rentbikes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author gabrielvega
 * @since 1.0.0
 * @version 1.0.0
 * created 2016-11-17
 */

public class RentRequest implements Parcelable {

    private String number;
    private String name;
    private String expiration;
    private String code;

    protected RentRequest(Parcel in) {
        number = in.readString();
        name = in.readString();
        expiration = in.readString();
        code = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(name);
        dest.writeString(expiration);
        dest.writeString(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RentRequest> CREATOR = new Creator<RentRequest>() {
        @Override
        public RentRequest createFromParcel(Parcel in) {
            return new RentRequest(in);
        }

        @Override
        public RentRequest[] newArray(int size) {
            return new RentRequest[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
