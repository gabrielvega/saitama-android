package com.saitama.rentbikes.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author gabrielvega
 * @since 1.0.0
 * @version 1.0.0
 * created 2016-11-17
 */

public class ResponseError implements Parcelable {

    private String code;
    private String message;

    protected ResponseError(Parcel in) {
        code = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResponseError> CREATOR = new Creator<ResponseError>() {
        @Override
        public ResponseError createFromParcel(Parcel in) {
            return new ResponseError(in);
        }

        @Override
        public ResponseError[] newArray(int size) {
            return new ResponseError[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
