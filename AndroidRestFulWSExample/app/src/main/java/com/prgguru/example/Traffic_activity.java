package com.prgguru.example;

import android.app.Activity;
import android.content.Context;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by PinkalJay on 5/5/15.
 */

public class Traffic_activity extends Activity
{

   private final Runnable mRunnable = new Runnable() {
        public void run() {

            Async thread = new Async();
            thread.execute();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traffic_layout);
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
    protected String FetchPhoneNo() {
        TelephonyManager tmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String mPN = tmgr.getLine1Number();
        //Log.d("phone number", mPN);

        return mPN;
    }

    public class Async extends AsyncTask<Void,Void,Void> {

        String adminno = "9169474486";
        String usage;
        private long lastTX =0, lastRX=0;

        private Handler mHandler = new Handler();
        private long mStartRX = 0;
        private long mStartTX = 0;
        private long total = 0;

        public Async(){
            mStartRX = TrafficStats.getMobileRxBytes();
            mStartTX = TrafficStats.getMobileTxPackets();
        }

        @Override
        protected Void doInBackground(Void... params1) {
            try {
                //pi changed +start and end date
                long rxBytes = TrafficStats.getMobileRxBytes() - mStartRX;
                long txBytes = TrafficStats.getMobileTxBytes() - mStartTX;

                //new code (avni correction for delta)
                rxBytes = rxBytes-lastRX;
                txBytes = txBytes-lastTX;
                lastRX = lastRX + rxBytes;
                lastTX = lastTX + txBytes;
                //

                total = rxBytes + txBytes;
                usage = total + "";
                RequestParams params = new RequestParams();
                params.put("phoneNo", adminno);
                params.put("usageMb", usage);
                invokeWS(params);
            }
            catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }
       @Override
       protected void onPostExecute(Void aVoid) {
           super.onPostExecute(aVoid);
       }
   }

    public void invokeWS(RequestParams params){

        // Show Progress Dialog
       // prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient objectvk
        AsyncHttpClient client = new AsyncHttpClient();
        //http://localhost:8055/login/doLogin?phoneNo=123&password=axc
        client.get("http://a06cbb35.ngrok.io/dataUsage/addData",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
               // prgDialog.hide();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                      //  Toast.makeText(getApplicationContext(), "XXXX!", Toast.LENGTH_LONG).show();
                       // Intent intent=new Intent(getApplicationContext(),Tab_mainActivity.class);
                       // startActivity(intent);
                    }
                    // Else display error message
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
