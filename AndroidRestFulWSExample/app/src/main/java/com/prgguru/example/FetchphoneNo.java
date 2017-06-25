package com.prgguru.example;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by PinkalJay on 4/14/15.
 */
public class FetchphoneNo extends Activity
{
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.setdata_layout);

       /* TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPN = tmgr.getLine1Number();
        Log.d("phone number", mPN);
        TextView PN = (TextView) findViewById(R.id.PN);
        PN.setText(mPN);*/

    }
 }
