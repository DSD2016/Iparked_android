package com.dsd2016.iparked_android.Fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsd2016.iparked_android.MyClasses.AnimatorUtils;
import com.dsd2016.iparked_android.MyClasses.Beacon;
import com.dsd2016.iparked_android.MyClasses.BeaconScanner;
import com.dsd2016.iparked_android.MyClasses.ClipRevealFrame;
import com.dsd2016.iparked_android.MyClasses.OnMenuItemSelectedListener;
import com.dsd2016.iparked_android.R;
import com.ogaclejapan.arclayout.ArcLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class MyPairingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PAIRING_FRAGMENT";
    Toast toast = null;
    ClipRevealFrame menuLayout;
    ArcLayout arcLayout;
    View centerItem;
    View rootLayout;
    ImageButton connectBtn;
    ImageView connectionImg;
    TextView distanceTxt;
    Boolean connect=false;
    private BeaconScanner beaconScanner;
    private OnMenuItemSelectedListener mListener;
    private int REQUEST_ENABLE_BT = 1;


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
        myView.findViewById(R.id.fab).setOnClickListener(this);

        connectBtn=(ImageButton)myView.findViewById(R.id.btn_connect);
        connectBtn.setOnClickListener(this);
        connectionImg=(ImageView)myView.findViewById(R.id.img_connection);
        distanceTxt=(TextView)myView.findViewById(R.id.txt_distance);

        //Useless code API 23 and bigger bug
        if (Build.VERSION.SDK_INT > 22) {
            getActivity().requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        beaconScanner = new BeaconScanner(getActivity());

        return myView;
    }

    //Useless code API 23 and bigger bug
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(requestCode == 1)
        {
            Log.d("Message", "coarse location permission granted");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!beaconScanner.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
           beaconScanner.scanForBeacons();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(beaconScanner.isScanning()) {
            beaconScanner.stopScanForBeacons();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                getActivity().finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static MyPairingFragment newInstance() {
        return new MyPairingFragment();
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            onFabClick(v);
            return;
        }
        if (v instanceof ImageButton) {
            if(v.getTag().toString().equals("connect")){
                if(!connect){
                    connect=true;
                    connectionImg.setImageResource(R.drawable.success);
                    distanceTxt.setText("Distance 20 Cm");
                    return;
                }
                else{
                    connect=false;
                    connectionImg.setImageResource(R.drawable.error);
                    distanceTxt.setText("Distance N/A");
                    return;
                }
            }

            switchfrag((ImageButton) v);
        }
    }
    private void switchfrag(ImageButton btn) {
        this.onClick(getView().findViewById(R.id.fab));
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

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<Beacon> beaconList;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            beaconList = new ArrayList<Beacon>();
            mInflator = MyPairingFragment.this.getActivity().getLayoutInflater();
        }

        public void addDevice(Beacon beacon) {
            for(Beacon b : beaconList) {
                if(b.getMajor() == beacon.getMajor() && b.getMinor() == beacon.getMinor()) {
                    beaconList.remove(b);
                    break;
                }
            }
            beaconList.add(beacon);
        }

        public Beacon getDevice(int position) {
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
        public View getView(int i, View view, ViewGroup viewGroup) {
//            ViewHolder viewHolder;
//            // General ListView optimization code.
//            if (view == null) {
//                view = mInflator.inflate(R.layout.listitem_device, null);
//                viewHolder = new ViewHolder();
//                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
//                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
//                view.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) view.getTag();
//            }
//
//            BluetoothDevice device = beaconList.get(i);
//            final String deviceName = device.getName();
//            if (deviceName != null && deviceName.length() > 0)
//                viewHolder.deviceName.setText(deviceName);
//            else
//                viewHolder.deviceName.setText(R.string.unknown_device);
//            viewHolder.deviceAddress.setText(device.getAddress());
//
            return view;
        }
    }
}
