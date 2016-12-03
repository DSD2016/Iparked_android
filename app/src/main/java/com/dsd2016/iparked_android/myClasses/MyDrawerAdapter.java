package com.dsd2016.iparked_android.myClasses;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsd2016.iparked_android.R;

import java.util.List;

/**
 * Created by Saeedek on 03-Dec-16.
 */

public class MyDrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public MyDrawerAdapter(Context context, int resource, List<DrawerItem> listItems) {
        super(context, resource, listItems);
        this.context = context;
        this.drawerItemList = listItems;
        this.layoutResID = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItemHolder drawerHolder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            drawerHolder = new DrawerItemHolder();

            view = inflater.inflate(layoutResID, parent, false);
            drawerHolder.ItemName = (TextView) view
                    .findViewById(R.id.txt_menuitem);
            drawerHolder.icon = (ImageView) view.findViewById(R.id.img_menuitem);

            view.setTag(drawerHolder);

        } else {
            drawerHolder = (DrawerItemHolder) view.getTag();

        }

        DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

        drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
                dItem.getImgResID()));
        drawerHolder.ItemName.setText(dItem.getItemName());

        return view;
    }
    private static class DrawerItemHolder {
        TextView ItemName;
        ImageView icon;
    }

}
