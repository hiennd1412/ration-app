package com.hiennd1412.ration.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.hiennd1412.ration.CustomInterface.UpLoadImageSuccessHandler;
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

public class Location_CreateNewActivity extends Location_aBaseActivity {

    private static final String TAG =  Location_CreateNewActivity.class.getSimpleName();


    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_location);

        tfDeliverPointName = (EditText) findViewById(R.id.tf_deliver_point_name);
        tfDeliverPointLocation = (EditText) findViewById(R.id.tf_deliver_point_location);
        tfDeliverPointPhonenumber = (EditText) findViewById(R.id.tf_phone_number);
        tfStartDate = (TextView) findViewById(R.id.tf_start_date);
        tfStartDate.setKeyListener(null);
        tfEndDate = (TextView) findViewById(R.id.tf_end_date);
        tfEndDate.setKeyListener(null);
        tfOpenTime = (TextView) findViewById(R.id.tf_open_time);
        tfOpenTime.setKeyListener(null);
        tfCloseTime = (TextView) findViewById(R.id.tf_close_time);
        tfCloseTime.setKeyListener(null);
        selectedImage = (ImageView) findViewById(R.id.location_image);

        tfStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Location_CreateNewActivity.this.hideKeyboard();
                new DatePickerDialog(Location_CreateNewActivity.this,
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
                                Location_CreateNewActivity.this.tfStartDate.setText(sdf.format(myCalendar.getTime()));
                                Location_CreateNewActivity.this.startDate = myCalendar.getTime();
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
                Location_CreateNewActivity.this.hideKeyboard();
                new DatePickerDialog(Location_CreateNewActivity.this,
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
                        Location_CreateNewActivity.this.tfEndDate.setText(sdf.format(myCalendar.getTime()));
                        Location_CreateNewActivity.this.endDate = myCalendar.getTime();
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

                Location_CreateNewActivity.this.hideKeyboard();
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Location_CreateNewActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                Location_CreateNewActivity.this.tfOpenTime.setText("" + selectedHour + ":" + selectedMinute);
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

                Location_CreateNewActivity.this.hideKeyboard();
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Location_CreateNewActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                Location_CreateNewActivity.this.tfCloseTime.setText("" + selectedHour + ":" + selectedMinute);
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

        String deliverPointName = this.tfDeliverPointName.getText().toString();
        String location = this.tfDeliverPointLocation.getText().toString();
        String phoneNumber = this.tfDeliverPointPhonenumber.getText().toString();
        String startDate = Utils.formatDateToSendToServer(Location_CreateNewActivity.this.startDate);
        String endDate = Utils.formatDateToSendToServer(Location_CreateNewActivity.this.endDate);
        String openTime = this.tfOpenTime.getText().toString();
        String closeTime = this.tfCloseTime.getText().toString();

        if (deliverPointName.equals("")) {
            Toast.makeText(Location_CreateNewActivity.this, "Chưa nhập tên địa điểm", Toast.LENGTH_SHORT).show();
            return;
        }
        if (location.equals("")) {
            Toast.makeText(Location_CreateNewActivity.this, "Chưa nhập địa chỉ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phoneNumber.equals("")) {
            Toast.makeText(Location_CreateNewActivity.this, "Chưa nhập số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (startDate.equals("")) {
//            Toast.makeText(Location_CreateNewActivity.this, "Chưa nhập ngày bắt đầu", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (endDate.equals("")) {
//            Toast.makeText(Location_CreateNewActivity.this, "Chưa nhập ngày kết thúc", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (openTime.equals("")) {
//            Toast.makeText(Location_CreateNewActivity.this, "Chưa nhập giờ mở cửa", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (closeTime.equals("")) {
//            Toast.makeText(Location_CreateNewActivity.this, "chưa nhập giờ đóng cửa", Toast.LENGTH_SHORT).show();
//            return;
//        }

        showProgressDialog();

        if (attachImageUri != null) {
            uploadImage(attachImageUri.getPath(), WebserviceInfors.uploadDeliverPointImage, new UpLoadImageSuccessHandler() {
                @Override
                public void onUploadImageSuccess(String imageName) {
                    Location_CreateNewActivity.this.addNewLocation(imageName);
                }
            });
        }
        else {
            addNewLocation("");
        }
    }

    public void addNewLocation(final String imageName) {

        if(!Utils.hasInternetConnection(Location_CreateNewActivity.this)) {
            Toast.makeText(Location_CreateNewActivity.this, Location_CreateNewActivity.this.getResources().getString(R.string.connect_internet_alert_message), Toast.LENGTH_SHORT).show();
            return;
        }
        showProgressDialog();
        RequestQueue queue = VolleyRequest.getInstance(Location_CreateNewActivity.this).getRequestQueue();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebserviceInfors.base_host_service + WebserviceInfors.createNewDeliverPoint,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressDialog();
                        Log.e(TAG, response);
                        Location_CreateNewActivity.this.hideProgressDialog();
                        try {
                            JSONObject jsResponse = new JSONObject(response);
                            if (jsResponse.getInt("code") == 999) {
                                Log.e(TAG, "success response: " + jsResponse);
                                JSONObject deliverPointJsonData = jsResponse.getJSONObject("data");
                                SharedPreferences.Editor sharePreferenceEditor = Utils.getCommonSharepreference(Location_CreateNewActivity.this).edit();
                                sharePreferenceEditor.putString(Location_CreateNewActivity.this.getResources().getString(R.string.choosen_location),deliverPointJsonData.toString());
                                sharePreferenceEditor.commit();
                                Location_CreateNewActivity.this.finish();
                            }
                            else {
                                Toast.makeText(Location_CreateNewActivity.this,jsResponse.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideProgressDialog();
                        Log.e(TAG, "onErrorResponse: " + error.toString());
                        Location_CreateNewActivity.this.hideProgressDialog();
                        Toast.makeText(Location_CreateNewActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("deliverPointName", Location_CreateNewActivity.this.tfDeliverPointName.getText().toString());
                params.put("phonenumber", Location_CreateNewActivity.this.tfDeliverPointPhonenumber.getText().toString());
                params.put("location", Location_CreateNewActivity.this.tfDeliverPointLocation.getText().toString());
                params.put("startDate", Utils.formatDateToSendToServer(Location_CreateNewActivity.this.startDate));
                params.put("endDate", Utils.formatDateToSendToServer(Location_CreateNewActivity.this.endDate));
                params.put("openTime", Location_CreateNewActivity.this.tfOpenTime.getText().toString());
                params.put("closeTime", Location_CreateNewActivity.this.tfCloseTime.getText().toString());
                params.put("image", imageName);
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
