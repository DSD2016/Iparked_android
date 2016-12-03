package com.dsd2016.iparked_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dsd2016.iparked_android.R;
import com.dsd2016.iparked_android.fragments.MyAboutFragment;
import com.dsd2016.iparked_android.fragments.MyMapFragment;
import com.dsd2016.iparked_android.fragments.MyPairingFragment;
import com.dsd2016.iparked_android.fragments.MySettingsFragment;
import com.dsd2016.iparked_android.myClasses.DrawerItem;
import com.dsd2016.iparked_android.myClasses.MyDrawerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private String[] mMenuTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private MyDrawerAdapter adapter;

    private List<DrawerItem> dataList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerTitle = getTitle();
        dataList = new ArrayList<DrawerItem>();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        dataList.add(new DrawerItem("Find My Car", R.drawable.find));
        dataList.add(new DrawerItem("Beacon Management", R.drawable.pair));
        dataList.add(new DrawerItem("Settings", R.drawable.settings));
        dataList.add(new DrawerItem("About Us", R.drawable.about));
        dataList.add(new DrawerItem("Exit", R.drawable.exit));

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        adapter = new MyDrawerAdapter(this, R.layout.drawer_list_item,
                dataList);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
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

    private class DrawerItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            selectItem(position);
        }


    }

    private void selectItem(int position) {
        switch (position){
            case 3:
                MyAboutFragment aboutfragment = new MyAboutFragment();

                /** In case this activity was started with special instructions from an
                 * Intent, pass the Intent's extras to the fragment as arguments */
                aboutfragment.setArguments(getIntent().getExtras());

                /** Add the fragment to the 'fragment_container' FrameLayout */
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, aboutfragment).commit();
                setTitle(R.string.abouttitle);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;

            case 0:
                MyMapFragment mapfragment = new MyMapFragment();

                /** In case this activity was started with special instructions from an
                 * Intent, pass the Intent's extras to the fragment as arguments */
                mapfragment.setArguments(getIntent().getExtras());

                /** Add the fragment to the 'fragment_container' FrameLayout */
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mapfragment).commit();
                setTitle(R.string.findtitle);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 2:
                MySettingsFragment settingsfragment = new MySettingsFragment();

                /** In case this activity was started with special instructions from an
                 * Intent, pass the Intent's extras to the fragment as arguments */
                settingsfragment.setArguments(getIntent().getExtras());

                /** Add the fragment to the 'fragment_container' FrameLayout */
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, settingsfragment).commit();
                setTitle(R.string.settingtitle);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 1:
                MyPairingFragment pairingfragment = new MyPairingFragment();

                /** In case this activity was started with special instructions from an
                 * Intent, pass the Intent's extras to the fragment as arguments */
                pairingfragment.setArguments(getIntent().getExtras());

                /** Add the fragment to the 'fragment_container' FrameLayout */
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, pairingfragment).commit();
                setTitle(R.string.pairtitle);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
