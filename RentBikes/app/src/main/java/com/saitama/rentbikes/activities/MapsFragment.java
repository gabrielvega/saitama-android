package com.saitama.rentbikes.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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
import java.util.HashMap;
import java.util.Map;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;

    private GoogleMap mMap;
    private Location mLastLocation;
    public LocationManager mLocationManager;
    int LOCATION_REFRESH_TIME = 0;
    int LOCATION_REFRESH_DISTANCE = 5;
    /**
     * Volley RequestQueue
     */
    RequestQueue requestQueue;
    private SupportMapFragment mapFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

//        mMapView = (MapView) rootView.findViewById(R.id.mapView2);
//        mMapView.onCreate(savedInstanceState);
//
//        mMapView.onResume(); // needed to get the map to display immediately
//
//        try {
//            MapsInitializer.initialize(getActivity().getApplicationContext());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        mMapView.getMapAsync(this);
//        getRentPlaces();
//        mLocationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.mapView);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.mapView, mapFragment).commit();
        } else {
            mapFragment.getMapAsync(this);
            getRentPlaces();
        }
    }

    public static MapsFragment newInstance() {

        MapsFragment fragment = new MapsFragment();
        /**
         * For future use
         */
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void getRentPlaces() {

        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String api = getString(R.string.url_base) + getString(R.string.places);

        final JsonObjectRequest placesRequest = new JsonObjectRequest(Request.Method.GET, api, new JSONObject(), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Gson gson = new Gson();
                PlacesResponse placesResponse = gson.fromJson(response.toString(),
                        PlacesResponse.class);

                if (placesResponse.getPlaces().size() < 1) {
                    Utils.toast("Sorry, try again.", 0, getActivity().getApplicationContext());
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
                    message = "Sorry, try again later.";
                }
                Utils.toast(message, 0, getActivity().getApplicationContext());


            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("content-type", "application/json");
                headers.put("Authorization", Utils.getAccessToken(getActivity().getApplicationContext()));
                return headers;
            }

        };


        //add to the request queue
        requestQueue.add(placesRequest);
    }

//    private final LocationListener mLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            code
//            System.out.println("onLocationChanged");
//
//            mLastLocation = location;
//
//            Log.i("GEO", "Latitude:" + String.valueOf(location.getLatitude()) + "\n" +
//                    "Longitude:" + String.valueOf(location.getLongitude()));
//            LatLng myPosition = new LatLng(location.getLatitude(), location.getLongitude());
//            mMap.addMarker(new MarkerOptions().position(myPosition).title(getString(R.string.my_position)));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(myPosition));
//        }

//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//    }
//};


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity().getApplicationContext(), getActivity().getLayoutInflater()));

        mMap.setOnInfoWindowClickListener(marker -> {

            Intent i = new Intent(getActivity().getApplicationContext(), RentBicycleActivity.class);
            i.putExtra(Const.PLACEID.getValue(), (String) marker.getTag());
            i.putExtra(Const.TITLE.getValue(), marker.getTitle());
            startActivity(i);

        });
    }

    @Override
    public void onResume() {
        super.onResume();
//        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
//        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
//        mMapView.onLowMemory();
    }
}
