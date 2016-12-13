package com.dsd2016.iparked_android.fragments;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.dsd2016.iparked_android.R;
import com.dsd2016.iparked_android.myClasses.Beacon;
import com.dsd2016.iparked_android.myClasses.Garage;
import com.dsd2016.iparked_android.myClasses.IparkedApp;
import com.dsd2016.iparked_android.myClasses.JsonBeacon;
import com.dsd2016.iparked_android.myClasses.MyLocationProvider;
import com.dsd2016.iparked_android.myClasses.OnGotLastLocation;
import com.dsd2016.iparked_android.myClasses.RestCommunicator;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.abs;

public class MyMapFragment extends Fragment implements OnMapReadyCallback, OnGotLastLocation {
    private static final String TAG = "MAP_FRAGMENT";
    View myView;

    protected MapView mapView;
    protected GoogleMap googleMap, map;
    MyLocationProvider myLocationProvider;
    private Map<String, Marker> markers;
    Bitmap floorMap;
    LatLng floorLocation;
    ArrayList<JsonBeacon> apiBeacons;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markers = new HashMap<>();
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(broadCastNewMessage, new IntentFilter("com.dsd2016.iparked_android.return_location"));
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadCastNewMessage);
    }

    @Override
    public void onDestroy() {
        if (mapView != null) {
            try {
                mapView.onDestroy();
            } catch (NullPointerException e) {
                Log.e(TAG, "Error while attempting MapView.onDestroy(), ignoring exception", e);
            }
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mapView != null) {
            mapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (MapView) myView.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        return myView;
    }

    public static MyMapFragment newInstance() {
        return new MyMapFragment();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        int hasLocationPermission = ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                showMessageOKCancel("You need to allow access to Location",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        modifyMap(googleMap);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this.getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void getGarage(){


        String url ="http://iparked-api.sytes.net/api/id/1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        Garage garage = gson.fromJson(response, Garage.class);
                        //floorMap = garage.getPlan(1, getContext());
                        floorLocation = garage.getFloorLocation(1);
                        apiBeacons = garage.getFloors().get(0).getBeacons();
                        String url ="http://iparked-api.sytes.net/api/floorplan/" + 1;
                        ImageRequest request = new ImageRequest(url,
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        floorMap = bitmap;
                                        LatLng fer_parking = new LatLng(45.800700, 15.971215);

                                        GroundOverlayOptions ferParkingMap = new GroundOverlayOptions()
                                                .image(BitmapDescriptorFactory.fromBitmap(floorMap))
                                                .position(fer_parking, 31, 62)
                                                .bearing(87);
                                        map.clear();
                                        map.addGroundOverlay(ferParkingMap);
                                    }
                                }, 0, 0, null,null,
                                new Response.ErrorListener() {
                                    public void onErrorResponse(VolleyError error) {
                                        floorMap = null;
                                    }
                                });
// Access the RequestQueue through your singleton class.
                        RestCommunicator.getInstance(getContext()).addToRequestQueue(request);

                        for (JsonBeacon b:apiBeacons){
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(b.getLatitude(),b.getLongitude()));
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.christmas_beacons));
                            markerOptions.title(b.getName());
                            markerOptions.snippet("Beacon "+b.getMinor_number()+"@ Floor "+b.getFloor_id());
                            map.addMarker(markerOptions);
                        }

                        MarkerOptions markerOptions3 = new MarkerOptions();
                        markerOptions3.position(new LatLng(45.800700, 15.971215));
                        markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.christmas_car));
                        markerOptions3.title("Santa parked his sleigh here");
                        map.addMarker(markerOptions3);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(45.800700, 15.971215), 19);
                        map.animateCamera(cameraUpdate);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        RestCommunicator.getInstance(getContext()).addToRequestQueue(stringRequest);


    }
    private void modifyMap(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(true);
        MapsInitializer.initialize(this.getContext());

        myLocationProvider = new MyLocationProvider(getContext(), this);
        floorLocation = new LatLng(20, 20);
        getGarage();


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    modifyMap(googleMap);
                } else {
                    // Permission Denied
                    Toast.makeText(this.getContext(), "Location Access Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private BroadcastReceiver broadCastNewMessage = new BroadcastReceiver() {
        /**
         * This method is called by OS when new broadcast is received. Here we are extracting
         * ArrayList of beacons from parcelable Object and then displaying that list with adapter.
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            if (map != null) {
                ArrayList<Beacon> beacons = IparkedApp.mDbHelper.getPersonalBeacons();

                /** Check if beacon list is not initialized */
                if (beacons == null) {
                    return;
                }

                /** Add beacons from database to map */
                for (Beacon beacon : beacons) {

                    Marker marker = markers.get(beacon.getAddress());
                    if (abs(beacon.getLocation().getLatitude()) <= 0.01 && abs(beacon.getLocation().getLongitude()) <= 0.01 && marker != null) {
                        marker.remove();
                        markers.remove(beacon.getAddress());
                    } else if ((abs(beacon.getLocation().getLatitude()) > 0.01 || abs(beacon.getLocation().getLongitude()) > 0.01) && marker == null) {
                        LatLng latLng = new LatLng(beacon.getLocation().getLatitude(), beacon.getLocation().getLongitude());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car2));
                        markerOptions.title(beacon.getName());
                        markerOptions.snippet("Parked on 12/11/2016 12:12:12");
                        Marker newMarker = map.addMarker(markerOptions);
                        markers.put(beacon.getAddress(), newMarker);
                    }
                }
            }
        }
    };

    public void onGotLastLocation(Location location) {
        LatLng fer_parking = new LatLng(45.800700, 15.971215);


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }

        map.setMyLocationEnabled(true);
    }

    public void CheckContinue(){
        myLocationProvider.Continue();
    }
}
