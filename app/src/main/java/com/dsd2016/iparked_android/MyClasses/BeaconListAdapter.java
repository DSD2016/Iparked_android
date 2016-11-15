package com.dsd2016.iparked_android.MyClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dsd2016.iparked_android.Fragments.MyPairingFragment;

import java.util.ArrayList;

/**
 * Created by Hrvoje on 14.11.2016..
 */

public abstract class BeaconListAdapter extends BaseAdapter{
        protected ArrayList<Beacon> beaconList;
        private LayoutInflater mInflator;

        public BeaconListAdapter(LayoutInflater mInflator) {
            super();
            beaconList = new ArrayList<>();
            this.mInflator = mInflator;
        }

        public void addBeacon(Beacon beacon) {
            for(Beacon b : beaconList) {
                if(b.getMajor() == beacon.getMajor() && b.getMinor() == beacon.getMinor()) {
                    beaconList.remove(b);
                    break;
                }
            }
            beaconList.add(beacon);
        }

        public Beacon getBeacon(int position) {
            return beaconList.get(position);
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

        @Override
        public abstract View getView(int i, View view, ViewGroup viewGroup);

}
