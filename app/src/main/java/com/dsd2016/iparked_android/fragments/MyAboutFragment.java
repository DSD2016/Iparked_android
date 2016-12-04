package com.dsd2016.iparked_android.fragments;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dsd2016.iparked_android.R;
import com.dsd2016.iparked_android.myClasses.IparkedApp;
import com.dsd2016.iparked_android.myClasses.RestCommunicator;


public class MyAboutFragment extends Fragment{


    private static final String TAG = "ABOUT_FRAGMENT";
    Toast toast = null;
    Button btn_insert,btn_location,btn_request;
    TextView txt_res;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @Override
    public void onResume() {

        super.onResume();
        /** FOR TESTING,Registering the broadcast receiver of the location, */
        getActivity().registerReceiver(broadCastNewMessage, new IntentFilter("com.dsd2016.iparked_android.return_location"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.fragment_about, container, false);

        btn_request = (Button) myView.findViewById(R.id.btn_req);
        txt_res = (TextView) myView.findViewById(R.id.txt_resp);
        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String url ="http://www.google.com";

                RequestQueue queue = RestCommunicator.getInstance(getContext()).
                        getRequestQueue();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                               txt_res.setText(response);
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
        });


        /** FOR TESTING,Inserting dummy info to the db */
        btn_insert=(Button)myView.findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long t;
                t=IparkedApp.mDbHelper.insert("Car 1",1,1,"11111","BF:A7:FC:FA:B2:F6", null);
                t=IparkedApp.mDbHelper.insert("Car 2",2,2,"22222","81:B6:2D:0B:FB:21", null);
                t=IparkedApp.mDbHelper.insert("Car 3",3,3,"33333","FC:64:FD:67:A8:CD", null);
                t=IparkedApp.mDbHelper.insert("Car 4",4,4,"44444","F5:12:A0:88:8E:F6", null);
                t=IparkedApp.mDbHelper.insert("Car 5",5,5,"55555","49:31:3A:3B:66:AE", null);

                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        /** FOR TESTING,getting the location. */

        btn_location=(Button)myView.findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().sendBroadcast(new Intent().setAction("com.dsd2016.iparked_android.get_location"));

            }
        });
        /** Building the menu */
        return myView;

    }
    /** FOR TESTING,Receiving the location */

    private BroadcastReceiver broadCastNewMessage = new BroadcastReceiver() {
        /**
         * This method is called by OS when new broadcast is received. Here we are extracting
         * ArrayList of beacons from parcelable Object and then displaying that list with adapter.
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v("iParked", "Return beacons");
            Location mylocation = intent.getExtras().getParcelable("location");
            Toast.makeText(context,String.valueOf(mylocation.getLatitude()), Toast.LENGTH_SHORT).show();

        }
    };
    public static MyAboutFragment newInstance() {
        return new MyAboutFragment();
    }
    public MyAboutFragment() {
    }

}
