package com.saitama.rentbikes.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.saitama.rentbikes.R;
import com.saitama.rentbikes.adapters.CustomInfoWindowAdapter;
import com.saitama.rentbikes.models.Place;
import com.saitama.rentbikes.models.PlacesResponse;
import com.saitama.rentbikes.models.ResponseError;
import com.saitama.rentbikes.utils.Const;
import com.saitama.rentbikes.utils.Utils;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    SupportMapFragment sMapFragment;
    View view;
    Date date;
    SharedPreferences.Editor editor;
    GoogleMap mMap;
    private RequestQueue requestQueue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Const.SHARED_PREFERENCES_NAME.getValue(), 0);
        sMapFragment = SupportMapFragment.newInstance();
        sMapFragment.setMenuVisibility(true);

        view = findViewById(R.id.content_frame);
        Snackbar.make(view, R.string.message_welcome_back, Snackbar.LENGTH_LONG)
                .show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sMapFragment.getMapAsync(this);
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();
        sFm.beginTransaction().replace(R.id.content_frame, sMapFragment).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitApp();
        }
    }


    /**
     * Nothing to show at the moment. Just for future use.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Nothing to show at the moment. Just for future use.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentManager sFm = getSupportFragmentManager();

        int id = item.getItemId();

        if (id == R.id.nav_map) {

//            if (!sMapFragment.isAdded())
            sFm.beginTransaction().replace(R.id.content_frame, sMapFragment).commit();
//            else
//                sFm.beginTransaction().show(sMapFragment).commit();


        } else if (id == R.id.nav_view_settings) {
            Snackbar.make(view, R.string.message_coming_soon, Snackbar.LENGTH_LONG)
                    .show();

        } else if (id == R.id.nav_view_help) {
            Snackbar.make(view, R.string.message_coming_soon, Snackbar.LENGTH_LONG)
                    .show();
        } else if (id == R.id.nav_exit) {
            exitApp();
        }

        // set the toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void exitApp() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.alert_title_logout)
                .setMessage(R.string.alert_message_logout)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    Utils.setAccessToken(getApplicationContext(), "");
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getApplicationContext(), getLayoutInflater()));

        mMap.setOnInfoWindowClickListener(marker -> {

            Intent i = new Intent(getApplicationContext(), RentBicycleActivity.class);
            i.putExtra(Const.PLACEID.getValue(), (String) marker.getTag());
            i.putExtra(Const.TITLE.getValue(), marker.getTitle());
            startActivity(i);

        });
        getRentPlaces();
    }

    private void getRentPlaces() {

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        String api = getString(R.string.url_base) + getString(R.string.places);

        final JsonObjectRequest placesRequest = new JsonObjectRequest(Request.Method.GET, api, new JSONObject(), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                PlacesResponse placesResponse = gson.fromJson(response.toString(),
                        PlacesResponse.class);

                if (placesResponse.getPlaces().size() < 1) {
                    Snackbar.make(view, R.string.message_error, Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    LatLng marker = null;
                    for (Place place : placesResponse.getPlaces()) {
                        marker = new LatLng(place.getLocation().getLat(), place.getLocation().getLng());
                        mMap.addMarker(new MarkerOptions().position(marker).title(place.getName())).setTag(place.getPlaceId());
                    }

                    //Just setting the map to the last marker
                    if (marker != null)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 10));


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                showProgress(false);
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
        requestQueue.add(placesRequest);
    }
}
