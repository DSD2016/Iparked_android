package com.dsd2016.iparked_android.fragments;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dsd2016.iparked_android.R;
import com.dsd2016.iparked_android.myClasses.Beacon;
import com.dsd2016.iparked_android.myClasses.BeaconDatabaseSchema;
import com.dsd2016.iparked_android.myClasses.BeaconListAdapter;
import com.dsd2016.iparked_android.myClasses.IparkedApp;

import java.util.ArrayList;

/**
 * This fragment lists out nearby beacons in clickable list. When user clicks on certain beacon, he
 * is offered to add that beacon as personal. Nearby beacons are acquired by sending Broadcast.
 * Broadcast is received by BeaconProximityService which than sends beacons back through another
 * Broadcast.
 */
public class MyPairingFragment extends ListFragment implements View.OnClickListener {

    private ListView listView;
    BeaconListAdapter beaconListAdapter;
    private ArrayList<Beacon> storedbeaconList = new ArrayList<>(1);
    private ArrayList<Beacon> visiblebeaconList = new ArrayList<>(1);



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public MyPairingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.fragment_pairing, container, false);
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
         */
        @Override
        public void onReceive(Context context, Intent intent) {

            visiblebeaconList.clear();
            visiblebeaconList = intent.getParcelableArrayListExtra("beaconList");
            populateListView();
        }
    };

    private void addStoredBeacon() {
        storedbeaconList.clear();
        Cursor c = IparkedApp.mDbHelper.read();
        String name,uuid,address;
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
            address = c.getString(
                    c.getColumnIndexOrThrow(BeaconDatabaseSchema.Beacons.COLUMN_ADDRESS));
            storedbeaconList.add(new Beacon(major,minor,name,uuid,stored,address));
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

    /**
     * Method is called every time something is clicked on the screen. If scan button is clicked Broadcast
     * is sent requesting nearby beacons.
     * @param v
     */
    @Override
    public void onClick(View v) {
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
                long insert = IparkedApp.mDbHelper.insert(name, beacon.getMajor(), beacon.getMinor(), beacon.getUuid(),beacon.getAddress(), beacon.getLocation());
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
                Boolean update = IparkedApp.mDbHelper.update(name, beacon.getAddress());
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

                Boolean delete = IparkedApp.mDbHelper.delete(beacon.getAddress());
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
}