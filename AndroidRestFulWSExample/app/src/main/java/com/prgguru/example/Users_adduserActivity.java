package com.prgguru.example;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PinkalJay on 4/5/15.
 */
public class Users_adduserActivity extends Activity {
    // Progress Dialog Object
    ProgressDialog prgDialog;
    // Error Msg TextView Object
    TextView errorMsg;
    //Store users names
    ArrayList<String> name_array = new ArrayList<String>();
    //store users phone number
    ArrayList<String> phone_array = new ArrayList<String>();
    ListView list;
    BaseAdapter2 adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row_listitem);
        list =  (ListView)findViewById(R.id.list_users);
        // Instantiate Progress Dialog object
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);
        //showUser();
    }

    public String FetchPhoneNo() {
        TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPN = tmgr.getLine1Number();
        Log.d("phone number", mPN);

        return mPN;
    }
    public void showUser(View view){

        String phoneNo = FetchPhoneNo();
        // Instantiate Http Request Param Object
        RequestParams params = new RequestParams();
        params.put("phonNo", "7543");
        // Invoke RESTful Web Service with Http parameters
        invokeWS(params);

    }

    public void invokeWS(RequestParams params){
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://a06cbb35.ngrok.io/list/familyUsers",params ,new AsyncHttpResponseHandler() {

            public void onSuccess(String response) {
                // Hide Progress Dialog
                prgDialog.hide();
                try{
                    JSONObject obj = new JSONObject(response);
                    if(obj.getBoolean("status")){
                      JSONArray new_array =  obj.getJSONArray("result");
                        System.out.print("result fetched= "+new_array.toString());
                        for (int i = 0, count = new_array.length(); i < count; i++) {
                            try {
                                JSONObject jsonObject = new_array.getJSONObject(i);
                                name_array.add(jsonObject.getString("username").toString());
                                phone_array.add(jsonObject.getString("Phone No").toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter = new BaseAdapter2(Users_adduserActivity.this, name_array, phone_array);
                        list.setAdapter(adapter);

                    }
                    // Else display error message
                    else{
                        errorMsg.setText(obj.getString("error_msg"));
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                }
                catch(JSONException e){
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog

            }
        });

    }

}