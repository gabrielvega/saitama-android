package com.saitama.rentbikes.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.saitama.rentbikes.R;
import com.saitama.rentbikes.models.ResponseError;
import com.saitama.rentbikes.models.SignupResponse;
import com.saitama.rentbikes.utils.Utils;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author gabrielvega
 * @version 1.0.0
 * created 2016-11-17
 * @since 1.0.0
 */

/**
 * A signup screen that offers signup via email/password.
 */
public class SignupActivity extends AppCompatActivity {

    /**
     * UI references
     */


    @BindView(R.id.signup_progress)
    protected View mProgressView;
    @BindView(R.id.signup_form)
    protected View mSignupFormView;
    @BindView(R.id.et_email)
    protected EditText etEmail;
    @BindView(R.id.et_password)
    protected EditText etPassword;
    @BindView(R.id.btn_signup)
    protected Button btnSignup;

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
     * Signup URL
     */
    private String api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        setupUI();
    }

    /**
     * Set up the signup form UI.
     */
    private void setupUI() {

        etPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == R.id.signup || id == EditorInfo.IME_NULL) {
                attemptSignup();
                return true;
            }
            return false;
        });

        btnSignup.setOnClickListener(view -> attemptSignup());
    }

    /**
     * Attempts signup the account specified by the signup form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual signup attempt is made.
     */
    private void attemptSignup() {

        // Reset errors.
        etEmail.setError(null);
        etPassword.setError(null);
        cancel = false;

        // Store values at the time of the signup attempt.
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            etEmail.setError(getString(R.string.error_field_required));
            focusView = etEmail;
            cancel = true;
        } else if (!Utils.isEmailValid(email)) {
            etEmail.setError(getString(R.string.error_invalid_email));
            focusView = etEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt signup and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user signup attempt.
            showProgress(true);

            String mEmail = etEmail.getText().toString().trim();
            String mPassword = etPassword.getText().toString().trim();


            try {

                requestQueue = Volley.newRequestQueue(getApplicationContext());
                api = getString(R.string.url_base) + getString(R.string.signup);

                Map<String, String> params = new HashMap<>();
                params.put("email", mEmail.trim());
                params.put("password", mPassword.trim());

                final JsonObjectRequest signupRequest = new JsonObjectRequest(Request.Method.POST, api, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        SignupResponse signupResponse = gson.fromJson(response.toString(),
                                SignupResponse.class);


                        if (signupResponse.getAccessToken() == null) {
                            Snackbar.make(mSignupFormView, R.string.message_error, Snackbar.LENGTH_LONG).show();
                        } else {
                            Utils.setAccessToken(getApplicationContext(), signupResponse.getAccessToken());
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
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
                            message = getString(R.string.message_error);
                        }
                        Snackbar.make(mSignupFormView, message, Snackbar.LENGTH_LONG).show();


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
                requestQueue.add(signupRequest);

            } catch (Exception e) {
                Utils.toast("Sorry, try again later.", 0, getApplicationContext());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showProgress(false);
    }

    /**
     * Shows the progress UI and hides the signup form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        Utils.showProgress(getApplicationContext(), mSignupFormView, mProgressView, show);

    }
}

