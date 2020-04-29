package com.hiennd1412.ration.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;
import com.hiennd1412.ration.Entity.AllocationModel;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;
import com.hiennd1412.ration.WebserviceGeneralManage.VolleyRequest;
import com.hiennd1412.ration.WebserviceGeneralManage.WebserviceInfors;
import com.hiennd1412.ration.adapter.ListAdapter_AllocationlList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckingGuestReceivedHistoryActivity extends BaseActivity {

    private static final String TAG = CheckingGuestReceivedHistoryActivity.class.getSimpleName();

    TextView tvPhoneNumber;
    TextView tvNoHistory;
    ListView listView;
    String phoneNumberToCheck;
    String identityNumberToCheck;
    String currentDeliverPointId;

    ListAdapter_AllocationlList listViewAdapter;

    ArrayList<AllocationModel> allocationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_phone_number_result);
        tvPhoneNumber = (TextView) findViewById(R.id.tv_number_to_check);
        tvNoHistory = (TextView) findViewById(R.id.tv_no_history);
        listView = (ListView) findViewById(R.id.lv_received_history);

        phoneNumberToCheck = getIntent().getStringExtra(getString(R.string.phone_number_to_check));
        identityNumberToCheck = getIntent().getStringExtra(getString(R.string.identity_number_to_check));
        currentDeliverPointId = getIntent().getStringExtra(getString(R.string.current_deliver_point));
        if(phoneNumberToCheck == null) {
            tvPhoneNumber.setText(identityNumberToCheck);
        }
        else {
            tvPhoneNumber.setText(phoneNumberToCheck);
        }
        setupListview();
        getHistory();
    }

    private void setupListview() {
        listView = findViewById(R.id.lv_received_history);
        listViewAdapter = new ListAdapter_AllocationlList(this);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }

    public void getHistory() {

        if(!Utils.hasInternetConnection(CheckingGuestReceivedHistoryActivity.this)) {
            Toast.makeText(CheckingGuestReceivedHistoryActivity.this, CheckingGuestReceivedHistoryActivity.this.getResources().getString(R.string.connect_internet_alert_message), Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog();
        RequestQueue queue = VolleyRequest.getInstance(CheckingGuestReceivedHistoryActivity.this).getRequestQueue();

        String serviceToCall = WebserviceInfors.checkGuestAllocatedWithPhoneNumber;
        if(phoneNumberToCheck == null) {
            serviceToCall = WebserviceInfors.checkGuestAllocatedWithIdentityNumber;
        }

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebserviceInfors.base_host_service + serviceToCall,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        CheckingGuestReceivedHistoryActivity.this.hideProgressDialog();
                        final ArrayList<AllocationModel> allocationList = new ArrayList<>();

                        try {
                            JSONObject jsResponse = new JSONObject(response);
                            if (jsResponse.getInt("code") == 999) {
                                allocationList.clear();
                                JSONArray jsonArray = jsResponse.getJSONArray("data");
                                Log.e(TAG, "success response: " + jsonArray);

                                if (jsonArray != null) {
                                    if(jsonArray.length() > 0) {
                                        for (int i=0; i < jsonArray.length(); i++ ){
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            Gson gson = new Gson();
                                            AllocationModel anAllocationModel = gson.fromJson(jsonObject.toString(), AllocationModel.class);
                                            allocationList.add(anAllocationModel);
                                        }
                                    }
                                }
                            }
                            else {
                                Toast.makeText(CheckingGuestReceivedHistoryActivity.this,jsResponse.getString("content"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        CheckingGuestReceivedHistoryActivity.this.allocationList = allocationList;
                        CheckingGuestReceivedHistoryActivity.this.listViewAdapter.setData(allocationList);
                        if (allocationList.size() == 0) {
                            tvNoHistory.setVisibility(View.VISIBLE);
                        }
                        else {
                            tvNoHistory.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CheckingGuestReceivedHistoryActivity.this.hideProgressDialog();
                        Log.e(TAG, "onErrorResponse: " + error.toString());
//                        Toast.makeText(CheckingPhoneNumberResultActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("guest_phone_number", CheckingGuestReceivedHistoryActivity.this.phoneNumberToCheck);
                params.put("guest_identify_number", CheckingGuestReceivedHistoryActivity.this.identityNumberToCheck);
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

    public void allocateForGuest() {

        if(!Utils.hasInternetConnection(CheckingGuestReceivedHistoryActivity.this)) {
            Toast.makeText(CheckingGuestReceivedHistoryActivity.this, CheckingGuestReceivedHistoryActivity.this.getResources().getString(R.string.connect_internet_alert_message), Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = VolleyRequest.getInstance(CheckingGuestReceivedHistoryActivity.this).getRequestQueue();
        showProgressDialog();

        String serviceToCall = WebserviceInfors.giveGiftForGuestWithPhoneNumber;

        if(phoneNumberToCheck == null) {
            serviceToCall = WebserviceInfors.giveGiftForGuestWithIdentifyNumber;
        }

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebserviceInfors.base_host_service + serviceToCall,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        CheckingGuestReceivedHistoryActivity.this.hideProgressDialog();
                        try {
                            JSONObject jsResponse = new JSONObject(response);
                            if (jsResponse.getInt("code") == 999) {
                                Log.e(TAG, "success response: " + jsResponse);

                                CheckingGuestReceivedHistoryActivity.this.getHistory();
                            }
                            else {
                                Toast.makeText(CheckingGuestReceivedHistoryActivity.this,jsResponse.getString("content"), Toast.LENGTH_SHORT).show();
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
                        CheckingGuestReceivedHistoryActivity.this.hideProgressDialog();
//                        Toast.makeText(CheckingPhoneNumberResultActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("guestPhoneNumber", CheckingGuestReceivedHistoryActivity.this.phoneNumberToCheck);
                params.put("guestIdentifyNumber", CheckingGuestReceivedHistoryActivity.this.identityNumberToCheck);
                params.put("deliverPoint", CheckingGuestReceivedHistoryActivity.this.currentDeliverPointId);
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

    public void onAllocateButtonClicked(View v) {
        if (currentDeliverPointId.equals("")) {
            Toast.makeText(this, "Bạn chưa chọn điểm làm việc", Toast.LENGTH_LONG).show();
            return;
        }
        allocateForGuest();
    }

    public void onRefuseButtonClicked(View v) {
        finish();
    }

}
