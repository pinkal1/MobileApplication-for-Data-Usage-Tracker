package com.prgguru.example;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by PinkalJay on 4/22/15.
 */
public class UserListAdapter extends BaseAdapter {
    private java.util.List mUserList;
    private LayoutInflater mInflater;
    private Context mContex;

    UserListAdapter(java.util.List<User> listUser, Context context){
        this.mUserList = listUser;
        this.mContex = context;
        mInflater = (LayoutInflater) mContex.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public User getItem(int position) {
        return (User) mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private TextView userName;
    private TextView phoneNumber;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View mView = convertView;
        mView = mInflater.inflate(R.layout.userlist_item, parent, false);
        userName = (TextView) mView.findViewById(R.id.txtUsername);
        phoneNumber = (TextView) mView.findViewById(R.id.txtPhonenum);

        User currentUser = getItem(position);

        userName.setText(currentUser.getUsername());
        phoneNumber.setText(currentUser.getPhoneNum());

        return mView;
    }

    public void updateUserList(java.util.List<User> userList){
        mUserList = userList;
        notifyDataSetChanged();
    }
}
