package com.hiennd1412.ration.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hiennd1412.ration.Entity.CallLogModel;
import com.hiennd1412.ration.Entity.DeliverPointModel;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;
import com.hiennd1412.ration.WebserviceGeneralManage.WebserviceInfors;
import com.hiennd1412.ration.adapter.ListAdapter_ReceivedCallList;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    public static final int MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 123;

    ListView listView;
    TextView tvCurrentLocation;
    ImageView deliverPointImage;

    ListAdapter_ReceivedCallList listViewAdapter;
    DeliverPointModel currentWorkingLocation;
    ArrayList<CallLogModel> listCallLog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        tvCurrentLocation = (TextView) findViewById(R.id.tvCurrentLocation);
        deliverPointImage = (ImageView) findViewById(R.id.deliver_point_image);
        setupListview();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillSelectedLocation();
        if ( checkAccessCallogPermission() ) {
            listCallLog = getCallDetails();
            listViewAdapter.setData(listCallLog);
        }
        else {
            requestAccessCallogPermission();
        }

    }

    private void requestAccessCallogPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.READ_CALL_LOG)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, MY_PERMISSIONS_REQUEST_READ_CALL_LOG);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions((Activity)this, new String[]{Manifest.permission.READ_CALL_LOG}, MY_PERMISSIONS_REQUEST_READ_CALL_LOG);
        }
    }

    private ArrayList<CallLogModel> getCallDetails() {

        ArrayList<CallLogModel> listCallLog = new ArrayList<>();

        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, CallLog.Calls.DATE + " DESC");
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);

            CallLogModel callLogModel;
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    callLogModel = new CallLogModel(phNumber, callDayTime, "INCOMING");
                    listCallLog.add(callLogModel);
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    callLogModel = new CallLogModel(phNumber, callDayTime, "MISSED");
                    listCallLog.add(callLogModel);
                    break;
            }
        }
        managedCursor.close();
        return listCallLog;
    }

    public boolean checkAccessCallogPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;

        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CALL_LOG:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(TAG, "call Details: " + getCallDetails());
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void fillSelectedLocation() {
        String currentWorkingLocationInfo = Utils.getCommonSharepreference(this).getString(getString(R.string.choosen_location), "");
        if (currentWorkingLocationInfo.equals("")) {
            tvCurrentLocation.setText("Bạn chưa chọn địa điểm làm việc");
        }
        else {
            Gson gson = new Gson();
            currentWorkingLocation = gson.fromJson(currentWorkingLocationInfo, DeliverPointModel.class);
            tvCurrentLocation.setText(currentWorkingLocation.deliverPointName + "\n" + currentWorkingLocation.location);
            final String imageLink = WebserviceInfors.base_host_service + WebserviceInfors.loadDeliverPointImage + currentWorkingLocation.image;
            Log.e(TAG, "fillSelectedLocation: " + imageLink );
            Glide.with(this)
                    .load(imageLink)
                    .placeholder(R.drawable.giving_to_charity_blog_size)
                    .into(deliverPointImage);
        }
    }

    private void setupListview() {
        listView = findViewById(R.id.lamp_listview);
        listViewAdapter = new ListAdapter_ReceivedCallList(this);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (currentWorkingLocation == null) {
                    Toast.makeText(HomeActivity.this, "Xin vui lòng chọn điểm làm việc để sử dụng tính năng này", Toast.LENGTH_LONG).show();
                    return;
                }

                CallLogModel aCallLog = HomeActivity.this.listCallLog.get(i);
                Intent intent = new Intent(HomeActivity.this, CheckingGuestReceivedHistoryActivity.class);
                intent.putExtra(getString(R.string.phone_number_to_check), aCallLog.callNumber);
                intent.putExtra(getString(R.string.current_deliver_point), currentWorkingLocation._id);
                startActivity(intent);
            }
        });
    }

    public void onAddNewLocationButtonClicked(View v) {
        Intent intent = new Intent(HomeActivity.this, Location_CreateNewActivity.class);
        startActivity(intent);
    }

    public void onChangeLocationButtonClicked(View v) {
        Intent intent = new Intent(HomeActivity.this, ChooseLocationActivity.class);
        startActivity(intent);
    }

}