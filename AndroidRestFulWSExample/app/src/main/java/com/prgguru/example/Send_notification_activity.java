package com.prgguru.example;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by PinkalJay on 5/12/15.
 */
public class Send_notification_activity extends Activity {

    private static final int NOTIFY_ME_ID=1337;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

                callAsynchronousTask();
    }


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            Async performBackgroundTask = new Async();
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {

                        }
                    }
                });
            }
        };

        timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 50000 ms
    }

    /*

        to fetch phone number

         */
     public String FetchPhoneNo() {
        TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPN = tmgr.getLine1Number();
        //Log.d("phone number", mPN);

        return mPN;
    }
    public class NotifyMessage extends Activity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            TextView txt=new TextView(this);
            txt.setText("Activity after click on notification");
            setContentView(txt);
        }
    }

    public class Async extends AsyncTask<Void,Void,Void> {

        private  final String adminno = FetchPhoneNo();
        private static final String STARTDATE="2015-04-23";
        private static final String ENDDATE="2015-05-30";

        @Override
        protected Void doInBackground(Void... params1) {
                    RequestParams params = new RequestParams();
                    params.put("phoneNo", adminno);
                    params.put("startDate", STARTDATE);
                    params.put("endDate", ENDDATE);
                    invokeWS(params);
                    createNotification(Send_notification_activity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Exceeded Data Limit!", Toast.LENGTH_LONG).show();


            //Intent intent=new Intent(getApplicationContext(),Turnoffdata_activity.class);
           //startActivity(intent);

        }


    }

    public void createNotification(Context context){
        final NotificationManager mgr=
                (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note=new Notification(R.drawable.android,
                "Android Example Status message!",
                System.currentTimeMillis());


        //  i= PendingIntent.get;
   Intent intent=new Intent(getApplicationContext(),Turnoffdata_activity.class);
     //   startActivity(intent);

        PendingIntent i = PendingIntent.getActivity(this,0, new Intent(this,Turnoffdata_activity.class),0);
        // This pending intent will open after notification click;
        //PendingIntent i=PendingIntent.getActivity(this, 0,
           //     new Intent(this, Turnoffdata_activity.class),
              //  0);

        note.setLatestEventInfo(this, "Data Share Plan application",
                "This is warning data usage reached above threshold", i);

        //After uncomment this line you will see number of notification arrived
        //note.number=2;
        mgr.notify(NOTIFY_ME_ID, note);

    }
    public void invokeWS(RequestParams params){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://a06cbb35.ngrok.io/dataUsage/thresholdNotify",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                        Toast.makeText(getApplicationContext(), "XXXX!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), obj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                // When Http response code is '404'
                if(statusCode == 404){
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if(statusCode == 500){
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else{
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
