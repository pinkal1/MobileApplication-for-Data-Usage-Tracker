package com.prgguru.example;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by PinkalJay on 5/7/15.
 */
public class Test_graph extends Activity {

    private ProgressBar pb;

    private static String PHONE_NUM = "";

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        String STARTDATE="2015-04-30";
        String ENDDATE="2015-05-30";

        user = (User) getIntent().getSerializableExtra("USEROBJECT");
        PHONE_NUM = user.getPhoneNum();

      //  PHONE_NUM="9169474486";
        RequestParams params = new RequestParams();
        params.put("phoneNo",PHONE_NUM);
        params.put("startDate",STARTDATE );
        params.put("endDate", ENDDATE);

        invokeWS(params);
    }
    public void invokeWS(RequestParams params){
        // Make RESTful webservice call using AsyncHttpClient objectvk
        AsyncHttpClient client = new AsyncHttpClient();

        client.get("http://a06cbb35.ngrok.io/dataUsage/dataRange",params ,new AsyncHttpResponseHandler() {
            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {

                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);
                    Double dataUsed = obj.getDouble("totalData");
                    // When the JSON response has status boolean value assigned with true
                    if(obj.getBoolean("status")){
                        Long[] Data = new Long[30];
                        JSONObject jsonData = obj.getJSONObject("resultData");


/*
                        for(int i =0; i<30; i++){
                            Data[i]=Long.valueOf(i*2);
                        }
*/

                        for(int i =0; i<30; i++){
                            Data[i]=Long.valueOf(0);
                        }

                        for(Iterator iterator = jsonData.keys(); iterator.hasNext();) {

                            String key = (String) iterator.next();
                            int val = (Integer)jsonData.get(key+"");
                            int k = Integer.parseInt(key);
                            Data[k]= Long.valueOf(val);

                            System.out.println(key + "\t" + val );
                        }

                        //for(int i =0; i<30; i++){
                          //  System.out.println(i + "\t" + Data[i]);
                       // }


                        GraphView graph = (GraphView) findViewById(R.id.graph);
                        BarGraphSeries<DataPoint> series = new BarGraphSeries<DataPoint>(new DataPoint[] {
                                new DataPoint(0, Data[0]),
                                new DataPoint(1, Data[1]),
                                new DataPoint(2, Data[2]),
                                new DataPoint(3, Data[3]),
                                new DataPoint(4, Data[4]),
                                new DataPoint(5, Data[5]),
                                new DataPoint(6, Data[6]),
                                new DataPoint(7, Data[7]),
                                new DataPoint(8, Data[8]),
                                new DataPoint(9, Data[9]),
                                new DataPoint(10, Data[10]),
                                new DataPoint(11, Data[11]),
                                new DataPoint(12, Data[12]),
                                new DataPoint(13, Data[13]),
                                new DataPoint(14, Data[14]),
                                new DataPoint(15, Data[15]),
                                new DataPoint(16, Data[16]),
                                new DataPoint(17, Data[17]),
                                new DataPoint(18, Data[18]),
                                new DataPoint(19, Data[19]),
                                new DataPoint(20, Data[20]),
                                new DataPoint(21, Data[21]),
                                new DataPoint(22, Data[22]),
                                new DataPoint(23, Data[23]),
                                new DataPoint(24, Data[24]),
                                new DataPoint(25, Data[25]),
                                new DataPoint(26, Data[26]),
                                new DataPoint(27, Data[27]),
                                new DataPoint(28, Data[28]),
                                new DataPoint(29, Data[29])
                        });

                        graph.addSeries(series);
                        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                            @Override
                            public int get(DataPoint data) {
                                return Color.rgb(0, 50, 50);
                            }
                        });

                        series.setSpacing(50);

                        // draw values on top
                        series.setDrawValuesOnTop(false);
                        //series.setValuesOnTopColor(Color.RED);

                        TextView view_usage_disp = (TextView)findViewById(R.id.Txt_view_usage_disp);
                        view_usage_disp.setText((obj.getString("totalData")));

                        pb=(ProgressBar)findViewById(R.id.progress_bar);

                        pb.setMax(obj.getInt("usersQuota"));
                        pb.setProgress(obj.getInt("usersQuota"));
                        pb.setSecondaryProgress(obj.getInt("usersThreshold"));
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
