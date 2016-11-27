package com.dsd2016.iparked_android.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.location.Location;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dsd2016.iparked_android.myClasses.AnimatorUtils;
import com.dsd2016.iparked_android.myClasses.ClipRevealFrame;
import com.dsd2016.iparked_android.myClasses.IparkedApp;
import com.dsd2016.iparked_android.myClasses.OnMenuItemSelectedListener;
import com.dsd2016.iparked_android.R;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;


public class MyAboutFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "ABOUT_FRAGMENT";
    Toast toast = null;
    ClipRevealFrame menuLayout;
    ArcLayout arcLayout;
    View centerItem;
    View rootLayout;
    Button btn_insert,btn_location;
    OnMenuItemSelectedListener mListener;
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
    @Override
    public void onResume() {

        super.onResume();
        /** FOR TESTING,Registering the broadcast receiver of the location, */
        getActivity().registerReceiver(broadCastNewMessage, new IntentFilter("com.dsd2016.iparked_android.return_location"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView=inflater.inflate(R.layout.fragment_about, container, false);
        menuLayout = (ClipRevealFrame) myView.findViewById(R.id.menu_layout);
        rootLayout = myView.findViewById(R.id.root_layout);
        arcLayout = (ArcLayout) myView.findViewById(R.id.arc_layout);
        centerItem = myView.findViewById(R.id.center_item);
        /** FOR TESTING,Inserting dummy info to the db */
        btn_insert=(Button)myView.findViewById(R.id.btn_insert);
        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Long t;
                t=IparkedApp.mDbHelper.Insert("Car 1",1,1,"11111");
                t=IparkedApp.mDbHelper.Insert("Car 2",2,2,"22222");
                t=IparkedApp.mDbHelper.Insert("Car 3",3,3,"33333");
                t=IparkedApp.mDbHelper.Insert("Car 4",4,4,"44444");
                t=IparkedApp.mDbHelper.Insert("Car 5",5,5,"55555");

                Toast.makeText(getActivity(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        /** FOR TESTING,getting the location. */

        btn_location=(Button)myView.findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().sendBroadcast(new Intent().setAction("com.dsd2016.iparked_android.get_location"));

            }
        });
        /** Building the menu */

        centerItem.setOnClickListener(this);
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }

        myView.findViewById(R.id.fab).setOnClickListener(this);
        return myView;

    }
    /** FOR TESTING,Receiving the location */

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

            Location mylocation = intent.getExtras().getParcelable("location");
            Toast.makeText(context,String.valueOf(mylocation.getLatitude()), Toast.LENGTH_SHORT).show();

        }
    };
    public static MyAboutFragment newInstance() {
        return new MyAboutFragment();
    }
    public MyAboutFragment() {
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            onFabClick(v);
            return;
        }
        if (v instanceof ImageButton) {
            switchfrag((ImageButton) v);
        }
    }
    /** Method for notifying the container to change the fragment */

    private void switchfrag(ImageButton btn) {
        mListener.onMenuItemSelected(btn.getTag().toString());
    }




    /** Menu Precedures */

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
