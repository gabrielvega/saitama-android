package com.saitama.rentbikes.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
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
import com.saitama.rentbikes.models.LoginResponse;
import com.saitama.rentbikes.models.ResponseError;
import com.saitama.rentbikes.utils.Utils;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gabrielvega
 * @version 1.0.0
 * created 2016-11-17
 * @since 1.0.0
 */

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText etEmail;
    private EditText etPassword;
    private View mProgressView;
    private View mLoginFormView;
    private int mStatusCode;
    /**
     * SharedPreferences editor
     */
    SharedPreferences.Editor editor;

    /**
     * Volley RequestQueue
     */
    RequestQueue requestQueue;

    boolean cancel = false;
    View focusView = null;

    /**
     * Login URL
     */
    private String api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        api = getString(R.string.url_base) + getString(R.string.signin);

        setContentView(R.layout.activity_login);
        // Set up the login form.
        etEmail = (EditText) findViewById(R.id.et_email);

        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        etEmail.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            etEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            String mEmail = etEmail.getText().toString().trim();
            String mPassword = etPassword.getText().toString().trim();


            try {

                Map<String, String> params = new HashMap<>();
                params.put("email", mEmail.trim());
                params.put("password", mPassword.trim());

                final JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, api, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        LoginResponse loginResponse = gson.fromJson(response.toString(),
                                LoginResponse.class);


                        if (loginResponse.getAccessToken() == null) {
                            Utils.toast("Sorry, try again.", 0, getApplicationContext());
                        } else {
                            Utils.setAccessToken(getApplicationContext(), loginResponse.getAccessToken());
                            Utils.toast("Welcome back!", 0, getApplicationContext());
                            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                            startActivity(i);
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
                        return headers;
                    }

                };


                //add to the request queue
                requestQueue.add(loginRequest);

            } catch (Exception e) {
                Utils.toast("Sorry, try again later.", 0, getApplicationContext());
            }
        }
    }

    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        Utils.showProgress(getApplicationContext(), mLoginFormView, mProgressView, show);

    }
}

