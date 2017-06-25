package com.prgguru.example;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by PinkalJay on 5/3/15.
 */
public class Manage extends ActionBarActivity {


    public void setCellularData(boolean enabled){
        try{
            final ConnectivityManager conman = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            Class conmanClass = Class.forName(conman.getClass().getName());
            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method[] methods = iConnectivityManagerClass.getDeclaredMethods();
            /*for(final Method method : methods){
                if(method.toGenericString().contains("set")){
                    Toast.makeText(getApplicationContext(),"Testing: Method" + method.getName(),Toast.LENGTH_LONG).show();
                }
            }*/
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(enabled);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
        }catch(ClassNotFoundException e){
            Toast.makeText(getApplicationContext(), "Exception ClassNotFound", Toast.LENGTH_SHORT).show();
        }catch (NoSuchFieldException e){
            Toast.makeText(getApplicationContext(), "Exception No Such File", Toast.LENGTH_SHORT).show();
        }catch (IllegalAccessException e){
            Toast.makeText(getApplicationContext(), "Exception Illegal Access", Toast.LENGTH_SHORT).show();
        }catch(IllegalArgumentException e){
            Toast.makeText(getApplicationContext(), "Exception Illegal Args", Toast.LENGTH_SHORT).show();
        }catch (NoSuchMethodException e){
            e.getCause();
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Exception No Such Method", Toast.LENGTH_SHORT).show();
        }catch (InvocationTargetException e){
            Toast.makeText(getApplicationContext(), "Exception Invocation", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_layout);
        final CheckBox wificheck = (CheckBox)findViewById(R.id.checkbox_Wifi);
        final CheckBox datacheck = (CheckBox)findViewById(R.id.checkbox_data);
        //final RadioGroup group =(RadioGroup)findViewById(R.id.radio_grp);
        final RadioButton radio2G = (RadioButton)findViewById(R.id.radio_2G);
        final RadioButton radio3G = (RadioButton)findViewById(R.id.radio_3G);
        final RadioButton radio4G = (RadioButton)findViewById(R.id.radio_4G);
        final Button btnMng = (Button)findViewById(R.id.btnmanagetab);
        boolean isWiFi=false;

        ConnectivityManager connMngr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = connMngr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWiFiConnected = wifiInfo.isConnected();

        if(isWiFiConnected){
            wificheck.setChecked(true);
        }else {
            wificheck.setChecked(false);
        }

        final WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wificheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox)v).isChecked();
                if(checked){
                    wifi.setWifiEnabled(true);
                    Toast.makeText(getApplicationContext(), "Wifi Turned On", Toast.LENGTH_SHORT).show();
                }
                else {
                    wifi.setWifiEnabled(false);
                    Toast.makeText(getApplicationContext(), "Wifi Turned Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        NetworkInfo dataInfo = connMngr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isDataConnected = dataInfo.isConnected();

        if(isDataConnected){
            datacheck.setChecked(true);
            radio2G.setEnabled(true);
            radio3G.setEnabled(true);
            radio4G.setEnabled(true);
        }else {
            datacheck.setChecked(false);
            radio2G.setEnabled(false);
            radio3G.setEnabled(false);
            radio4G.setEnabled(false);
        }

        datacheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox)v).isChecked();
                if(checked){
                    setCellularData(true);
                    radio2G.setEnabled(true);
                    radio3G.setEnabled(true);
                    radio4G.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Cellular Data Turned On", Toast.LENGTH_SHORT).show();
                }
                else {
                    setCellularData(false);
                    radio2G.setEnabled(false);
                    radio3G.setEnabled(false);
                    radio4G.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Cellular Data Turned Off", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final String network = getNetworkClass();
        if(network == "4G"){
            radio4G.setChecked(true);
        }else if(network == "3G"){
            radio3G.setChecked(true);
        }else if (network == "2G"){
            radio2G.setChecked(true);
        }else if (network == "None"){
            Toast.makeText(getApplicationContext(), "No Network Found!", Toast.LENGTH_SHORT).show();
        }

        radio2G.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Do not click!!", Toast.LENGTH_SHORT).show();
                if(network == "4G"){
                    radio4G.setChecked(true);
                }else if(network == "3G"){
                    radio3G.setChecked(true);
                }else if (network == "2G"){
                    radio2G.setChecked(true);
                }else if (network == "None"){
                    Toast.makeText(getApplicationContext(), "No Network Found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        radio3G.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Do not click!!", Toast.LENGTH_SHORT).show();
                if(network == "4G"){
                    radio4G.setChecked(true);
                }else if(network == "3G"){
                    radio3G.setChecked(true);
                }else if (network == "2G"){
                    radio2G.setChecked(true);
                }else if (network == "None"){
                    Toast.makeText(getApplicationContext(), "No Network Found!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        radio4G.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Do not click!!", Toast.LENGTH_SHORT).show();
                if(network == "4G"){
                    radio4G.setChecked(true);
                }else if(network == "3G"){
                    radio3G.setChecked(true);
                }else if (network == "2G"){
                    radio2G.setChecked(true);
                }else if (network == "None"){
                    Toast.makeText(getApplicationContext(), "No Network Found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMng.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                startActivityForResult(intent, 1);
            }
        });
    }

    public String getNetworkClass() {
        TelephonyManager mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
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
}