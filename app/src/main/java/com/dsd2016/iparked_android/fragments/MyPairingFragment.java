package com.dsd2016.iparked_android.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.ListFragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.ListView;

import android.widget.Toast;

import com.dsd2016.iparked_android.myClasses.AnimatorUtils;
import com.dsd2016.iparked_android.myClasses.Beacon;
import com.dsd2016.iparked_android.myClasses.BeaconDatabaseSchema;
import com.dsd2016.iparked_android.myClasses.BeaconListAdapter;
import com.dsd2016.iparked_android.myClasses.ClipRevealFrame;
import com.dsd2016.iparked_android.myClasses.IparkedApp;
import com.dsd2016.iparked_android.myClasses.OnMenuItemSelectedListener;
import com.dsd2016.iparked_android.myClasses.ParcelableBeaconList;
import com.dsd2016.iparked_android.R;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * This fragment lists out nearby beacons in clickable list. When user clicks on certain beacon, he
 * is offered to add that beacon as personal. Nearby beacons are acquired by sending Broadcast.
 * Broadcast is received by BeaconProximityService which than sends beacons back through another
 * Broadcast.
 */
public class MyPairingFragment extends ListFragment implements View.OnClickListener {

    private static final String TAG = "PAIRING_FRAGMENT";
    Toast toast = null;
    ClipRevealFrame menuLayout;
    ArcLayout arcLayout;
    View centerItem;
    View rootLayout;
    private ListView listView;
    BeaconListAdapter beaconListAdapter;
    private ArrayList<Beacon> storedbeaconList = new ArrayList<Beacon>(1);
    private ArrayList<Beacon> visiblebeaconList = new ArrayList<Beacon>(1);
    private ArrayList<Beacon> allbeaconList = new ArrayList<Beacon>(1);

    private OnMenuItemSelectedListener mListener;


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

    public MyPairingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.fragment_pairing, container, false);
        rootLayout = myView.findViewById(R.id.root_layout);

        menuLayout = (ClipRevealFrame) myView.findViewById(R.id.menu_layout);
        arcLayout = (ArcLayout) myView.findViewById(R.id.arc_layout);
        centerItem = myView.findViewById(R.id.center_item);
        centerItem.setOnClickListener(this);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }
        myView.findViewById(R.id.scan_button).setOnClickListener(this);
        myView.findViewById(R.id.fab).setOnClickListener(this);

        listView = (ListView) myView.findViewById(android.R.id.list);

        beaconListAdapter = new BeaconListAdapter(getActivity().getLayoutInflater(),this);

        return myView;
    }

    /**
     * Every time fragment is resumed broadcast receiver is registered(It is unregistered
     * in onPause method). And the list of beacons is cleared.
     */
    @Override
    public void onResume() {

        super.onResume();

        getActivity().registerReceiver(broadCastNewMessage, new IntentFilter("com.dsd2016.iparked_android.return_beacons"));
        listView.setAdapter(beaconListAdapter);
        populateListView();

    }

    private void populateListView() {
        beaconListAdapter.clear();
        addStoredBeacon();
        beaconListAdapter.addAll(storedbeaconList);
        beaconListAdapter.addAll(visiblebeaconList);
        beaconListAdapter.notifyDataSetChanged();
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

            Log.v("iParked", "Return beacons");
            visiblebeaconList.clear();
            visiblebeaconList = intent.getParcelableArrayListExtra("BeaconList");
           // ParcelableBeaconList parcelableBeaconList = intent.getParcelableExtra("BeaconList");
           // ArrayList<Beacon> beaconList = (ArrayList<Beacon>)parcelableBeaconList.getbeaconList();

            populateListView();

        }
    };

    private void addStoredBeacon() {
        storedbeaconList.clear();
        Cursor c = IparkedApp.mDbHelper.Read();
        String name,uuid;
        int major,minor,stored;
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            name = c.getString(
                    c.getColumnIndexOrThrow(BeaconDatabaseSchema.Beacons.COLUMN_NAME));
            uuid = c.getString(
                    c.getColumnIndexOrThrow(BeaconDatabaseSchema.Beacons.COLUMN_UUID));
            major = c.getInt(
                    c.getColumnIndexOrThrow(BeaconDatabaseSchema.Beacons.COLUMN_MAJOR));
            minor = c.getInt(
                    c.getColumnIndexOrThrow(BeaconDatabaseSchema.Beacons.COLUMN_MINOR));
            stored = c.getInt(
                    c.getColumnIndexOrThrow(BeaconDatabaseSchema.Beacons.COLUMN_STORED));
            storedbeaconList.add(new Beacon(name,major,minor,uuid,stored));
        }
    }

    /**
     * Every time fragment is paused broadcast receiver is unregistered(It is registered
     * in onResume method).
     */
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(broadCastNewMessage);
    }


    public static MyPairingFragment newInstance() {
        return new MyPairingFragment();
    }

    /**
     * Method is called every time something is clicked on the screen. If scan button is clicked Broadcast
     * is sent requesting nearby beacons.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                onFabClick(v);
                break;
            case R.id.scan_button:
                getActivity().sendBroadcast(new Intent().setAction("com.dsd2016.iparked_android.get_beacons"));
                break;
            default:
                switchfrag((ImageButton) v);
                break;
        }
    }

    public void addBeacon(final Beacon beacon){                                                       // Add beacon to Database
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());        // shows a dialog to give a custom name to the beacon
        builder.setTitle("Enter a name for this Beacon");

        final EditText input = new EditText(getContext());
        input.setText(beacon.getName());
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                long insert = IparkedApp.mDbHelper.Insert(name, beacon.getMajor(), beacon.getMinor(), beacon.getUuid());
                if(insert!=-1){
                    Toast.makeText(getContext(), "Beacon Successfully Saved", Toast.LENGTH_SHORT).show();
                    populateListView();
                }
                else{
                    Toast.makeText(getContext(), "Error during saving", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void editBeacon(final Beacon beacon){                                         // Edit the selected beacon
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());        // shows a dialog to modify the custom name
        builder.setMessage("Edit the Beacon Name");

        final EditText input = new EditText(getContext());
        input.setText(beacon.getName());
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                Boolean update = IparkedApp.mDbHelper.Update(name, beacon.getUuid());
                if(update){
                    Toast.makeText(getContext(), "Beacon Successfully Updated", Toast.LENGTH_SHORT).show();
                    populateListView();
                }
                else{
                    Toast.makeText(getContext(), "Error during updating", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
        beaconListAdapter.notifyDataSetChanged();
    }

    public void deleteBeacon(final Beacon beacon){                                       // delete selected beacon from database
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());        // shows a dialog to confirm deletion
        builder.setTitle("Are you sure to delete this Beacon?");


        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Boolean delete = IparkedApp.mDbHelper.Delete(beacon.getUuid());
                if(delete){
                    Toast.makeText(getContext(), "Beacon Successfully Deleted", Toast.LENGTH_SHORT).show();
                    populateListView();
                }
                else{
                    Toast.makeText(getContext(), "Error during deleting", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    private void switchfrag(ImageButton btn) {
        mListener.onMenuItemSelected(btn.getTag().toString());
    }

    private void onFabClick(View v) {
        int x = (v.getLeft() + v.getRight()) / 2;
        int y = (v.getTop() + v.getBottom()) / 2;
        float radiusOfFab = 1f * v.getWidth() / 2f;
        float radiusFromFabToRoot = (float) Math.hypot(
                Math.max(x, rootLayout.getWidth() - x),
                Math.max(y, rootLayout.getHeight() - y));

        if (v.isSelected()) {
            hideMenu(x, y, radiusFromFabToRoot, radiusOfFab);
        } else {
            showMenu(x, y, radiusOfFab, radiusFromFabToRoot);
        }
        v.setSelected(!v.isSelected());
    }
    private void showMenu(int cx, int cy, float startRadius, float endRadius) {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        Animator revealAnim = createCircularReveal(menuLayout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);

        animList.add(revealAnim);
        animList.add(createShowItemAnimator(centerItem));

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }
        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();
    }
    private void hideMenu(int cx, int cy, float startRadius, float endRadius) {
        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        animList.add(createHideItemAnimator(centerItem));

        Animator revealAnim = createCircularReveal(menuLayout, cx, cy, startRadius, endRadius);
        revealAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        revealAnim.setDuration(200);
        revealAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });

        animList.add(revealAnim);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animList);
        animSet.start();

    }
    private Animator createShowItemAnimator(View item) {
        float dx = centerItem.getX() - item.getX();
        float dy = centerItem.getY() - item.getY();

        item.setScaleX(0f);
        item.setScaleY(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(0f, 1f),
                AnimatorUtils.scaleY(0f, 1f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(50);
        return anim;
    }
    private Animator createHideItemAnimator(final View item) {
        final float dx = centerItem.getX() - item.getX();
        final float dy = centerItem.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.scaleX(1f, 0f),
                AnimatorUtils.scaleY(1f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.setInterpolator(new DecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });
        anim.setDuration(50);
        return anim;
    }
    private Animator createCircularReveal(final ClipRevealFrame view, int x, int y, float startRadius,
                                          float endRadius) {
        final Animator reveal;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            reveal = ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, endRadius);
        } else {
            view.setClipOutLines(true);
            view.setClipCenter(x, y);
            reveal = ObjectAnimator.ofFloat(view, "ClipRadius", startRadius, endRadius);
            reveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setClipOutLines(false);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        return reveal;
    }
}