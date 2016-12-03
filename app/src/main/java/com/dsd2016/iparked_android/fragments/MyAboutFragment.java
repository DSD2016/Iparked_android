package com.dsd2016.iparked_android.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dsd2016.iparked_android.myClasses.AnimatorUtils;
import com.dsd2016.iparked_android.myClasses.ClipRevealFrame;
import com.dsd2016.iparked_android.myClasses.IparkedApp;
import com.dsd2016.iparked_android.myClasses.OnMenuItemSelectedListener;
import com.dsd2016.iparked_android.R;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;


public class MyAboutFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "ABOUT_FRAGMENT";
    Toast toast = null;
    Button btn_insert,btn_location;
    OnMenuItemSelectedListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity=(Activity)context;
        try {
            mListener = (OnMenuItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMenuItemSelectedListener");
        }
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


        IparkedApp.mMenuHandler.setElements(myView.findViewById(R.id.root_layout),getContext());
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

        myView.findViewById(R.id.fab).setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            IparkedApp.mMenuHandler.handleMenu();
            return;
        }

    }
    /** Method for notifying the container to change the fragment */






    /** Menu Precedures */




}
