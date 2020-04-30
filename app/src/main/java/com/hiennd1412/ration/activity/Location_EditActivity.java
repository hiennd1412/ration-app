package com.hiennd1412.ration.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;
import com.hiennd1412.ration.WebserviceGeneralManage.VolleyRequest;
import com.hiennd1412.ration.WebserviceGeneralManage.WebserviceInfors;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Location_EditActivity extends Location_aBaseActivity {

    private static final String TAG =  "AddNewLocationActivity";

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);

        tfDeliverPointName = (EditText) findViewById(R.id.tf_deliver_point_name);
        tfDeliverPointLocation = (EditText) findViewById(R.id.tf_deliver_point_location);
        tfStartDate = (EditText) findViewById(R.id.tf_start_date);
        tfStartDate.setKeyListener(null);
        tfEndDate = (EditText) findViewById(R.id.tf_end_date);
        tfEndDate.setKeyListener(null);
        tfOpenTime = (EditText) findViewById(R.id.tf_open_time);
        tfOpenTime.setKeyListener(null);
        tfCloseTime = (EditText) findViewById(R.id.tf_close_time);
        tfCloseTime.setKeyListener(null);

        tfStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Location_EditActivity.this.hideKeyboard();
                new DatePickerDialog(Location_EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO Auto-generated method stub
                                myCalendar.set(Calendar.YEAR, year);
                                myCalendar.set(Calendar.MONTH, monthOfYear);
                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                String myFormat = "dd/MM/yyyy"; //In which you need put here
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                Location_EditActivity.this.tfStartDate.setText(sdf.format(myCalendar.getTime()));
                                Location_EditActivity.this.startDate = myCalendar.getTime();
                            }
                        },
                        myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tfEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Location_EditActivity.this.hideKeyboard();
                new DatePickerDialog(Location_EditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "dd/MM/yyyy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                        Location_EditActivity.this.tfEndDate.setText(sdf.format(myCalendar.getTime()));
                        Location_EditActivity.this.endDate = myCalendar.getTime();
                    }
                },
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        tfOpenTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Location_EditActivity.this.hideKeyboard();
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Location_EditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                Location_EditActivity.this.tfOpenTime.setText("" + selectedHour + ":" + selectedMinute);
                            }
                        },
                        9,
                        00,
                        true);
                mTimePicker.setTitle("Chọn giờ mở cửa");
                mTimePicker.show();

            }
        });

        tfCloseTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Location_EditActivity.this.hideKeyboard();
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Location_EditActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                Location_EditActivity.this.tfCloseTime.setText("" + selectedHour + ":" + selectedMinute);
                            }
                        },
                        16,
                        00,
                        true);
                mTimePicker.setTitle("Chọn giờ mở cửa");
                mTimePicker.show();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackButtonClicked();
                break;
        }
        return true;
    }

    public void onSaveButtonClicked(View v){
        editLocation();
    }

    public void editLocation() {

        if(!Utils.hasInternetConnection(Location_EditActivity.this)) {
            Toast.makeText(Location_EditActivity.this, Location_EditActivity.this.getResources().getString(R.string.connect_internet_alert_message), Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = VolleyRequest.getInstance(Location_EditActivity.this).getRequestQueue();
        showProgressDialog();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebserviceInfors.base_host_service + WebserviceInfors.createNewDeliverPoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        Location_EditActivity.this.hideProgressDialog();
                        try {
                            JSONObject jsResponse = new JSONObject(response);
                            if (jsResponse.getInt("code") == 999) {
                                Log.e(TAG, "success response: " + jsResponse);
                            }
                            else {
                                Toast.makeText(Location_EditActivity.this,jsResponse.getString("content"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.toString());
                        Location_EditActivity.this.hideProgressDialog();
//                        Toast.makeText(CheckingPhoneNumberResultActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deliverPointName", Location_EditActivity.this.tfDeliverPointName.getText().toString());
                params.put("location", Location_EditActivity.this.tfDeliverPointLocation.getText().toString());
                params.put("startDate", Utils.formatDateToSendToServer(Location_EditActivity.this.startDate));
                params.put("endDate", Utils.formatDateToSendToServer(Location_EditActivity.this.endDate));
                params.put("openTime", Location_EditActivity.this.tfOpenTime.getText().toString());
                params.put("closeTime", Location_EditActivity.this.tfCloseTime.getText().toString());
                return params;
            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<>();
//                SharedPreferences sharedPreferences = Utils.getCommonSharepreference(CheckingPhoneNumberResultActivity.this);
//                params.put("x-access-token", sharedPreferences.getString(getResources().getString(R.string.login_token),""));
//                return params;
//            }

        };
        queue.getCache().clear();
        queue.add(stringRequest);

    }

}
