package com.dsd2016.iparked_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dsd2016.iparked_android.fragments.MyAboutFragment;
import com.dsd2016.iparked_android.fragments.MyMapFragment;
import com.dsd2016.iparked_android.fragments.MyPairingFragment;
import com.dsd2016.iparked_android.fragments.MySettingsFragment;
import com.dsd2016.iparked_android.myClasses.OnMenuItemSelectedListener;
import com.dsd2016.iparked_android.R;

public class MainActivity extends AppCompatActivity implements OnMenuItemSelectedListener {
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {

            /** However, if we're being restored from a previous state,
            * then we don't need to do anything and should return or else
            * we could end up with overlapping fragments. */
            if (savedInstanceState != null) {
                return;
            }

            /** Create a new Fragment to be placed in the activity layout */
            MyMapFragment firstFragment = new MyMapFragment();

            /** In case this activity was started with special instructions from an
            * Intent, pass the Intent's extras to the fragment as arguments */
            firstFragment.setArguments(getIntent().getExtras());

            /** Add the fragment to the 'fragment_container' FrameLayout */
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    @Override
    public void onMenuItemSelected(String frag) {
        switch (frag){
            case "about":
                MyAboutFragment aboutfragment = new MyAboutFragment();

                /** In case this activity was started with special instructions from an
                * Intent, pass the Intent's extras to the fragment as arguments */
                aboutfragment.setArguments(getIntent().getExtras());

                /** Add the fragment to the 'fragment_container' FrameLayout */
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, aboutfragment).commit();
                break;
            case "find":
                MyMapFragment mapfragment = new MyMapFragment();

                /** In case this activity was started with special instructions from an
                * Intent, pass the Intent's extras to the fragment as arguments */
                mapfragment.setArguments(getIntent().getExtras());

                /** Add the fragment to the 'fragment_container' FrameLayout */
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mapfragment).commit();
                break;
            case "settings":
                MySettingsFragment settingsfragment = new MySettingsFragment();

                /** In case this activity was started with special instructions from an
                * Intent, pass the Intent's extras to the fragment as arguments */
                settingsfragment.setArguments(getIntent().getExtras());

                /** Add the fragment to the 'fragment_container' FrameLayout */
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, settingsfragment).commit();

                break;
            case "pair":
                MyPairingFragment pairingfragment = new MyPairingFragment();

                /** In case this activity was started with special instructions from an
                * Intent, pass the Intent's extras to the fragment as arguments */
                pairingfragment.setArguments(getIntent().getExtras());

                /** Add the fragment to the 'fragment_container' FrameLayout */
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, pairingfragment).commit();

                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            /** Check for the integer request code originally supplied to startResolutionForResult(). */
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        MyMapFragment temp=(MyMapFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        temp.CheckContinue();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(this, "App won't work without location enabled", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }

    }
}
