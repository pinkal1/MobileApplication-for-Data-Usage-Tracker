package com.prgguru.example;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by PinkalJay on 4/1/15.
 */
public class ManageFragmentTab extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.manage_layout, container, false);

        final RadioButton radio2G = (RadioButton)rootView.findViewById(R.id.radio_2G);
        final RadioButton radio3G = (RadioButton)rootView.findViewById(R.id.radio_3G);
        final RadioButton radio4G = (RadioButton)rootView.findViewById(R.id.radio_4G);
        //final Button btnMng = (Button)getActivity().findViewById(R.id.btnmanagetab);

        String network = getNetworkClass();
        if(network == "4G"){
            radio4G.setChecked(true);
        }else if(network == "3G"){
            radio3G.setChecked(true);
        }else if (network == "2G"){
            radio2G.setChecked(true);
        }else if (network == "None"){
            Toast.makeText(getActivity().getApplicationContext(), "No Network Found!", Toast.LENGTH_SHORT).show();
        }
        radio2G.setEnabled(false);
        radio3G.setEnabled(false);
        radio4G.setEnabled(false);


        return rootView;

    }

    public String getNetworkClass() {
        TelephonyManager mTelephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                //Toast.makeText(getApplicationContext(), "No Network Found!", Toast.LENGTH_SHORT).show();
                return "None";
        }
    }

    public void onRadioButtonClicked(View view)
    {
        Toast.makeText(getActivity().getApplicationContext(), "No Network Found!", Toast.LENGTH_SHORT).show();
        boolean checked = ((RadioButton)view).isChecked();
        switch (view.getId())
        {
            case R.id.radio_4G:
                if (checked)
                    break;
            case R.id.radio_3G:
                if (checked)
                    break;
            case R.id.radio_2G:
                if (checked)
                    break;
        }

    }
    }