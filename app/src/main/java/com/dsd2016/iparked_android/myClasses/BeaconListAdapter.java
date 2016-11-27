package com.dsd2016.iparked_android.myClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dsd2016.iparked_android.R;
import com.dsd2016.iparked_android.fragments.MyMapFragment;
import com.dsd2016.iparked_android.fragments.MyPairingFragment;

import java.util.ArrayList;



public class BeaconListAdapter extends BaseAdapter{

    protected ArrayList<Beacon> beaconList;
    private LayoutInflater mInflator;
    private MyPairingFragment fragment;

    public BeaconListAdapter(LayoutInflater mInflator, MyPairingFragment fragment) {
        super();
        this.fragment = fragment;
        this.mInflator = mInflator;
        beaconList = new ArrayList<>();
    }

    public void addAll(ArrayList<Beacon> beaconList) {
        for (Beacon beacon : beaconList) {
            this.beaconList.add(beacon);
        }
    }
    public void clear() {
        beaconList.clear();
    }
    @Override
    public int getCount() {
        return beaconList.size();
    }

    @Override
    public Object getItem(int i) {
        return beaconList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     *Class that holds beacon GUI
     */
    static class ViewHolder {
        TextView beaconName;
        TextView beaconUuid;
        TextView beaconNumbers;
        TextView beaconDistance;
        TextView beaconAddress;
        ImageButton adddelBeacon, editBeacon;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final int index=i;
        ViewHolder viewHolder;
        // General ListView optimization code.
        if (view == null) {
            view = mInflator.inflate(R.layout.beacon_list_view, viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.beaconName = (TextView) view.findViewById(R.id.beacon_name);
            viewHolder.beaconUuid = (TextView) view.findViewById(R.id.beacon_uuid);
            viewHolder.beaconNumbers = (TextView) view.findViewById(R.id.beacon_numbers);
            viewHolder.beaconDistance = (TextView) view.findViewById(R.id.beacon_distance);
            viewHolder.beaconAddress = (TextView) view.findViewById(R.id.beacon_address);
            viewHolder.adddelBeacon = (ImageButton) view.findViewById(R.id.btn_add_beacon);
            viewHolder.adddelBeacon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.getTag().toString().equals("delete")){
                        fragment.deleteBeacon(beaconList.get(index));
                    }
                    else{
                        fragment.addBeacon(beaconList.get(index));
                    }
                }
            });
            viewHolder.editBeacon = (ImageButton) view.findViewById(R.id.btn_edit_beacon);
            viewHolder.editBeacon.setVisibility(View.INVISIBLE);
            viewHolder.editBeacon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fragment.editBeacon(beaconList.get(index));
                }
            });

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Beacon beacon = beaconList.get(i);
        final String deviceName = beacon.getName();
        if (deviceName != null && deviceName.length() > 0) {
            viewHolder.beaconName.setText("Name: "+deviceName);
        }else {
            viewHolder.beaconName.setText("Name: unknown");
        }
        viewHolder.beaconUuid.setText("UUID:"+ beacon.getUuid());
        viewHolder.beaconNumbers.setText("Major: " + beacon.getMajor() + "  Minor: " + beacon.getMinor());
        viewHolder.beaconDistance.setText("Distance: " + beacon.getDistance());
        viewHolder.beaconAddress.setText("Address: " + beacon.getAddress());
        if(beacon.getStored()==1){                                                     // Add button enabled only if beacon not stored
            viewHolder.editBeacon.setVisibility(View.VISIBLE);
            viewHolder.adddelBeacon.setImageResource(R.drawable.b_del);
            viewHolder.adddelBeacon.setTag("delete");
        }else{
            viewHolder.editBeacon.setVisibility(View.INVISIBLE);
            viewHolder.adddelBeacon.setImageResource(R.drawable.b_add);
            viewHolder.adddelBeacon.setTag("add");
        }

        return view;
    }

}
