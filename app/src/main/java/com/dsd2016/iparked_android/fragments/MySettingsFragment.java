package com.dsd2016.iparked_android.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dsd2016.iparked_android.R;
import com.dsd2016.iparked_android.myClasses.IparkedApp;
import com.github.machinarius.preferencefragment.PreferenceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySettingsFragment extends PreferenceFragment{

    private static final String TAG = "SETTINGS_FRAGMENT";
    Toast toast = null;
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

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myView = inflater.inflate(R.layout.fragment_settings, container, false);
        return myView;

    }
    public static MySettingsFragment newInstance() {
        return new MySettingsFragment();
    }


}
