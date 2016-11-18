package com.saitama.rentbikes.utils;

/**
 * @author gabrielvega
 * @since 1.0.0
 * @version 1.0.0
 * created 2016-11-17
 */

public enum Const {
    SHARED_PREFERENCES_NAME("4g308rbeji24g8hwei32f0h8ewi"),
    ACCESSTOKEN("accesstoken"),
    LOGINRESPONSE("loginresponse"),
    APPROVEDAMOUNT("approvedamount"),
    REJECTEDAMOUNT("rejectedamount"),
    APPROVEDCHARGES("approvedcharges"),
    REJECTEDCHARGES("rejectedcharges"),
    CLIENTS("clients"),
    DEVICE_TOKEN("devicetoken"),
    BLOCKED("blocked"),
    LEAVINGAPP("leavingapp"),
    PIN("pin");

    private final String value;

    private Const(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
