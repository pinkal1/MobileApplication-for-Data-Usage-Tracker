package com.prgguru.example;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by PinkalJay on 4/6/15.
 */
public class Date_activity extends Activity implements View.OnClickListener {

    public static final String TAG = "Date_activity";

    // views
    private TextView txtDate;
   // private TextView txtTime;
    private Button btnChangeDate;
    //private Button btnChangeTime;

    // variables to store the selected date and time
    private int mSelectedYear;
    private int mSelectedMonth;
    private int mSelectedDay;
   // private int mSelectedHour;
    //private int mSelectedMinutes;

    // CallBacks for date and time pickers
    private DatePickerDialog.OnDateSetListener mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // update the current variables ( year, month and day)
            mSelectedDay = dayOfMonth;
            mSelectedMonth = monthOfYear;
            mSelectedYear = year;

            // update txtDate with the selected date
            updateDateUI();
        }
    };

   /*private TimePickerDialog.OnTimeSetListener mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // update the current variables (hour and minutes)
            mSelectedHour = hourOfDay;
            mSelectedMinutes = minute;

            // update txtTime with the selected time
            updateTimeUI();
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_layout);

        // retrieve views
        this.txtDate = (TextView) findViewById(R.id.txt_date);
       // this.txtTime = (TextView) findViewById(R.id.txt_time);
        this.btnChangeDate = (Button) findViewById(R.id.btn_show_date_picker);
       // this.btnChangeTime = (Button) findViewById(R.id.btn_show_time_picker);
        this.btnChangeDate.setOnClickListener(this);
       // this.btnChangeTime.setOnClickListener(this);

        // initialize the current date
        Calendar calendar = Calendar.getInstance();
        this.mSelectedYear = calendar.get(Calendar.YEAR);
        this.mSelectedMonth = calendar.get(Calendar.MONTH);
        this.mSelectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        //this.mSelectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        //this.mSelectedMinutes = calendar.get(Calendar.MINUTE);

        // set the current date and time on TextViews
        updateDateUI();
       // updateTimeUI();

    }
    private void updateDateUI() {
        String month = ((mSelectedMonth+1) > 9) ? ""+(mSelectedMonth+1): "0"+(mSelectedMonth+1) ;
        String day = ((mSelectedDay) < 10) ? "0"+mSelectedDay: ""+mSelectedDay ;
        txtDate.setText(month+"/"+day+"/"+mSelectedYear);
    }
    /*private void updateTimeUI() {
        String hour = (mSelectedHour > 9) ? ""+mSelectedHour: "0"+mSelectedHour ;
        String minutes = (mSelectedMinutes > 9) ?""+mSelectedMinutes : "0"+mSelectedMinutes;
        txtTime.setText(hour+":"+minutes);
    }*/
    // initialize the DatePickerDialog
    private DatePickerDialog showDatePickerDialog(int initialYear, int initialMonth, int initialDay, DatePickerDialog.OnDateSetListener listener) {
        DatePickerDialog dialog = new DatePickerDialog(this, listener, initialYear, initialMonth, initialDay);
        dialog.show();
        return dialog;
    }
    // initialize the TimePickerDialog
    /*private TimePickerDialog showTimePickerDialog(int initialHour, int initialMinutes, boolean is24Hour, TimePickerDialog.OnTimeSetListener listener) {
        TimePickerDialog dialog = new TimePickerDialog(this, listener, initialHour, initialMinutes, is24Hour);
        dialog.show();
        return dialog;
    }
*/

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_show_date_picker:
                showDatePickerDialog(mSelectedYear, mSelectedMonth, mSelectedDay, mOnDateSetListener);
                break;
            /*case R.id.btn_show_time_picker:
                showTimePickerDialog(mSelectedHour, mSelectedMinutes, true, mOnTimeSetListener);
                break;*/
        }
    }
}
