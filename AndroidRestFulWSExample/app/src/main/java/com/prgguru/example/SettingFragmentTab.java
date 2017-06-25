package com.prgguru.example;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by PinkalJay on 4/1/15.
 */
public class SettingFragmentTab extends Fragment
{
   Button buttonuser;
   Button buttonpassword ;
   Button buttonbillingcycle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting_layout, container, false);
        buttonuser = (Button) rootView.findViewById(R.id.btnUserClick);
        buttonuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users_onclick();
            }
        });


        buttonpassword = (Button) rootView.findViewById(R.id.btnpasswordClick);
        buttonpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password_onclick();
            }
        });


        buttonbillingcycle = (Button) rootView.findViewById(R.id.btn_billingcylcle);
        buttonbillingcycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingcycle_onclick();
            }
        });
        return rootView;

    }

    public void users_onclick() {
        Intent i = new Intent(getActivity(),User_List_activity.class);
        startActivity(i);
    }

    public void password_onclick()
    {
        Intent i=new Intent(getActivity(), User_changepasswordActivity.class);
        startActivity(i);
    }

    public void billingcycle_onclick()
    {
        Intent i=new Intent(getActivity(), Date_activity.class);
        startActivity(i);
    }
    /*public void Billingcycle_onclick(View view)
    {
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);

    }*/
//    public void Billingcycles_onclick(View view)
//    {

//        Intent password_resetlayoutIntent=new Intent(getApplicationContext(),Forget_passwordActivity1.class);
//        password_resetlayoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(password_resetlayoutIntent);
//    }
    /*public void Billing_onclick(View view)
    {
        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }
*/

}