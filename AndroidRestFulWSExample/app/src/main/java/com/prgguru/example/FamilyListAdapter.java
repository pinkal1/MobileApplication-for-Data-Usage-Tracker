package com.prgguru.example;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by PinkalJay on 5/6/15.
 */
public class FamilyListAdapter extends BaseAdapter {
    private java.util.List mFamilyList;
    private LayoutInflater mInflater;
    private Context mContex;

    FamilyListAdapter(java.util.List<User> listUser, Context context){
        this.mFamilyList = listUser;
        this.mContex = context;
        mInflater = (LayoutInflater) mContex.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mFamilyList.size();
    }

    @Override
    public User getItem(int position) {
        return (User) mFamilyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private TextView userName;
    private TextView phoneNumber;
    private ProgressBar pb;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        mView = mInflater.inflate(R.layout.familyreplist_item, parent, false);
        userName = (TextView) mView.findViewById(R.id.txtUsername);
        phoneNumber = (TextView) mView.findViewById(R.id.txtPhonenum);

        User currentUser = getItem(position);

        userName.setText(currentUser.getUsername());
        phoneNumber.setText(currentUser.getPhoneNum());
        pb=(ProgressBar)mView.findViewById(R.id.prog_bar);
        pb.setMax(10);
        pb.setProgress(4);
        pb.setSecondaryProgress(8);

        return mView;
    }

    public void updateFamilyList(java.util.List<User> familyList){
        mFamilyList = familyList;
        notifyDataSetChanged();
    }
}
