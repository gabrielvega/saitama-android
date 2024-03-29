package com.saitama.rentbikes.activities;

import android.annotation.TargetApi;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RentBicycleActivity extends AppCompatActivity {

    // UI Elements
    @BindView(R.id.tv_place_title)
    TextView tvPlaceTitle;
    @BindView(R.id.et_card_number)
    EditText etCardNumber;
    @BindView(R.id.et_name_on_card)
    EditText etNameOnCard;
    @BindView(R.id.et_month)
    EditText etMonth;
    @BindView(R.id.et_year)
    EditText etYear;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_pay)
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
        getDataIntent();
        ButterKnife.bind(this);
        setupUI();


    }

    private void getDataIntent() {
        placeId = getIntent().getStringExtra(Const.PLACEID.getValue());
        placeTitle = getIntent().getStringExtra(Const.TITLE.getValue());
    }

    /**
     * Setup the UI elements
     */
    private void setupUI() {

        formView = findViewById(R.id.view_pay);
        mProgressView = findViewById(R.id.view_progress_pay);
        tvPlaceTitle.setText(placeTitle);

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attempPay();

            }
        });

    }

    private void attempPay() {
        showProgress(true);
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        //Current date in format yyyymm
        int currentDate = (currentYear * 100) + currentMonth;

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
        //Expiration date in format yyyymm
        int expiration = 0;
        if (!TextUtils.isEmpty(year) && !TextUtils.isEmpty(month))
            expiration = ((Integer.parseInt(year) + 2000) * 100) + Integer.parseInt(month);

        if (TextUtils.isEmpty(code)) {
            etCode.setError(getString(R.string.error_field_required));
            focusView = etCode;
            cancel = true;
        } else if (code.length() != 3) {
            etCode.setError(getString(R.string.error_invalid_code));
            focusView = etCode;
            cancel = true;
        }


        if (TextUtils.isEmpty(year)) {
            etYear.setError(getString(R.string.error_field_required));
            focusView = etYear;
            cancel = true;
        } else {

            if (expiration < currentDate) {
                etMonth.setError(getString(R.string.error_invalid_month));
                focusView = etMonth;
                cancel = true;
            }

            if (year.length() != 2 || ((2000 + Integer.parseInt(year) < currentYear))) {
                etYear.setError(getString(R.string.error_invalid_year));
                focusView = etYear;
                cancel = true;
            }
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

        if (TextUtils.isEmpty(nameOnCard)) {
            etNameOnCard.setError(getString(R.string.error_field_required));
            focusView = etNameOnCard;
            cancel = true;
        }


        if (TextUtils.isEmpty(cardNumber)) {
            etCardNumber.setError(getString(R.string.error_field_required));
            focusView = etCardNumber;
            cancel = true;
        } else if (cardNumber.length() != 16) {
            etCardNumber.setError(getString(R.string.error_invalid_card_number));
            focusView = etCardNumber;
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
                        Utils.toast(getString(R.string.message_error), 0, getApplicationContext());
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
