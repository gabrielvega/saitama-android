package com.saitama.rentbikes.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by gabrielvega on 2016-11-17.
 */
@Deprecated
public class CustomObjectRequest extends JsonObjectRequest {

    private int mStatusCode;

    public CustomObjectRequest(int method, String url, JSONObject jsonObject, Response.Listener<JSONObject> listener,
                               Response.ErrorListener errorListener) {
        super(method, url, jsonObject, listener, errorListener);
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        mStatusCode = response.statusCode;
        return super.parseNetworkResponse(response);
    }

}
