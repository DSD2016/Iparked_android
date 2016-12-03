package com.dsd2016.iparked_android.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dsd2016.iparked_android.myClasses.AnimatorUtils;
import com.dsd2016.iparked_android.myClasses.ClipRevealFrame;
import com.dsd2016.iparked_android.myClasses.IparkedApp;
import com.dsd2016.iparked_android.myClasses.OnMenuItemSelectedListener;
import com.dsd2016.iparked_android.R;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySettingsFragment extends PreferenceFragment implements View.OnClickListener {

    private static final String TAG = "SETTINGS_FRAGMENT";
    Toast toast = null;
    OnMenuItemSelectedListener mListener;
    private ListPreference mListPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }

    public MySettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            mListener = (OnMenuItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnMenuItemSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_settings, container, false);
        myView.findViewById(R.id.fab).setOnClickListener(this);
        IparkedApp.mMenuHandler.setElements(myView.findViewById(R.id.root_layout), getContext());
        return myView;

    }

    public static MySettingsFragment newInstance() {
        return new MySettingsFragment();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            IparkedApp.mMenuHandler.handleMenu();
        }
    }

}
