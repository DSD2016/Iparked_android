package com.dsd2016.iparked_android.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dsd2016.iparked_android.Fragments.MyAboutFragment;
import com.dsd2016.iparked_android.Fragments.MyMapFragment;
import com.dsd2016.iparked_android.Fragments.MyPairingFragment;
import com.dsd2016.iparked_android.Fragments.MySettingsFragment;
import com.dsd2016.iparked_android.MyClasses.OnMenuItemSelectedListener;
import com.dsd2016.iparked_android.R;

public class MainActivity extends AppCompatActivity implements OnMenuItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            MyMapFragment firstFragment = new MyMapFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, firstFragment).commit();
        }

    }

    @Override
    public void onMenuItemSelected(String frag) {
        switch (frag){
            case "about":
                MyAboutFragment aboutfragment = new MyAboutFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                aboutfragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, aboutfragment).commit();
                break;
            case "find":
                MyMapFragment mapfragment = new MyMapFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                mapfragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mapfragment).commit();
                break;
            case "settings":
                MySettingsFragment settingsfragment = new MySettingsFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                settingsfragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, settingsfragment).commit();

                break;
            case "pair":
                MyPairingFragment pairingfragment = new MyPairingFragment();

                // In case this activity was started with special instructions from an
                // Intent, pass the Intent's extras to the fragment as arguments
                pairingfragment.setArguments(getIntent().getExtras());

                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, pairingfragment).commit();

                break;
        }
    }
}
