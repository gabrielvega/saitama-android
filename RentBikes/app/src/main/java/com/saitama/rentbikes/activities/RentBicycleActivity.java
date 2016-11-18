package com.saitama.rentbikes.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.saitama.rentbikes.R;
import com.saitama.rentbikes.models.RentResponse;
import com.saitama.rentbikes.models.ResponseError;
import com.saitama.rentbikes.utils.Const;
import com.saitama.rentbikes.utils.Utils;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RentBicycleActivity extends AppCompatActivity {

    // UI Elements
    TextView tvPlaceTitle;
    EditText etCardNumber;
    EditText etNameOnCard;
    EditText etMonth;
    EditText etYear;
    EditText etCode;
    Button btnPay;

    View mProgressView;
    View formView;

    String placeId;
    String placeTitle;

    boolean cancel = false;
    View focusView = null;

    /**
     * Volley RequestQueue
     */
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_bicycle);
        placeId = getIntent().getStringExtra(Const.PLACEID.getValue());
        placeTitle = getIntent().getStringExtra(Const.TITLE.getValue());

        setUI();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attempPay();

            }
        });

    }

    /**
     * Setup the UI elements
     */
    private void setUI() {

        tvPlaceTitle = (TextView) findViewById(R.id.tv_place_title);
        etCardNumber = (EditText) findViewById(R.id.et_card_number);
        etNameOnCard = (EditText) findViewById(R.id.et_name_on_card);
        etMonth = (EditText) findViewById(R.id.et_month);
        etYear = (EditText) findViewById(R.id.et_year);
        etCode = (EditText) findViewById(R.id.et_code);
        btnPay = (Button) findViewById(R.id.btn_pay);

        formView = findViewById(R.id.view_pay);
        mProgressView = findViewById(R.id.view_progress_pay);

        tvPlaceTitle.setText(placeTitle);

    }

    private void attempPay() {
        showProgress(true);
        etCardNumber.setError(null);
        etNameOnCard.setError(null);
        etMonth.setError(null);
        etYear.setError(null);
        etCode.setError(null);
        cancel = false;

        // Store values at the time of the login attempt.
        String cardNumber = etCardNumber.getText().toString().trim();
        String nameOnCard = etNameOnCard.getText().toString().trim();
        String month = etMonth.getText().toString().trim();
        String year = etYear.getText().toString().trim();
        String code = etCode.getText().toString().trim();

        if (TextUtils.isEmpty(cardNumber)) {
            etCardNumber.setError(getString(R.string.error_field_required));
            focusView = etCardNumber;
            cancel = true;
        } else if (cardNumber.length() != 16) {
            etCardNumber.setError(getString(R.string.error_invalid_card_number));
            focusView = etCardNumber;
            cancel = true;
        }

        if (TextUtils.isEmpty(nameOnCard)) {
            etNameOnCard.setError(getString(R.string.error_field_required));
            focusView = etNameOnCard;
            cancel = true;
        }

        if (TextUtils.isEmpty(month)) {
            etMonth.setError(getString(R.string.error_field_required));
            focusView = etMonth;
            cancel = true;
        } else if (month.length() != 2 | Integer.parseInt(month) < 1 | Integer.parseInt(month) > 12) {
            etMonth.setError(getString(R.string.error_invalid_month));
            focusView = etMonth;
            cancel = true;
        }

        if (TextUtils.isEmpty(year)) {
            etYear.setError(getString(R.string.error_field_required));
            focusView = etYear;
            cancel = true;
        } else if (year.length() != 2) {
            etYear.setError(getString(R.string.error_invalid_year));
            focusView = etYear;
            cancel = true;
        }

        if (TextUtils.isEmpty(code)) {
            etCode.setError(getString(R.string.error_field_required));
            focusView = etCode;
            cancel = true;
        } else if (code.length() != 3) {
            etCode.setError(getString(R.string.error_invalid_code));
            focusView = etCode;
            cancel = true;
        }

        if (cancel) {
            showProgress(false);
            focusView.requestFocus();

        } else {

            requestQueue = Volley.newRequestQueue(getApplicationContext());
            String api = getString(R.string.url_base) + getString(R.string.rent);
            Map<String, String> params = new HashMap<>();
            params.put("number", cardNumber);
            params.put("name", nameOnCard);
            params.put("expiration", month + "/" + year);
            params.put("code", code);

            final JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, api, new JSONObject(params), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Gson gson = new Gson();
                    RentResponse rentResponse = gson.fromJson(response.toString(),
                            RentResponse.class);


                    if (rentResponse.getMessage() == null) {
                        Utils.toast("Sorry, try again.", 0, getApplicationContext());
                    } else {
                        Utils.toast(rentResponse.getMessage(), 0, getApplicationContext());
                        finish();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showProgress(false);
                    String message = "";
                    try {
                        String stringResponse = new String(error.networkResponse.data, Charset.forName("UTF-8"));
                        Gson gson = new Gson();
                        ResponseError responseError = gson.fromJson(stringResponse,
                                ResponseError.class);
                        message = responseError.getMessage() + " (" + responseError.getCode() + ")";
                    } catch (Exception e) {
                        message = "Sorry, try again later.";
                    }
                    Utils.toast(message, 0, getApplicationContext());


                }
            }) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("content-type", "application/json");
                    headers.put("Authorization", Utils.getAccessToken(getApplicationContext()));
                    return headers;
                }

            };

            //add to the request queue
            requestQueue.add(loginRequest);

        }


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        Utils.showProgress(getApplicationContext(), formView, mProgressView, show);

    }
}
