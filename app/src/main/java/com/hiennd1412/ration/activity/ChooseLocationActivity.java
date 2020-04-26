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
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.gson.Gson;
import com.hiennd1412.ration.Entity.DeliverPointModel;
import com.hiennd1412.ration.Entity.SessionModel;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;
import com.hiennd1412.ration.WebserviceGeneralManage.VolleyRequest;
import com.hiennd1412.ration.WebserviceGeneralManage.WebserviceInfors;
import com.hiennd1412.ration.adapter.ListAdapter_DeliverPointList;
import com.hiennd1412.ration.adapter.ListAdapter_ReceivedCallList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class ChooseLocationActivity extends BaseActivity {

    private static final String TAG = ChooseLocationActivity.class.getSimpleName();

    ListView listView;
    ListAdapter_DeliverPointList listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        setupListview();
        getDeliverPointList();
    }

    private void setupListview() {
        listView = findViewById(R.id.location_listview);
        listViewAdapter = new ListAdapter_DeliverPointList(this);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void getDeliverPointList() {
        showProgressDialog();
        if(!Utils.hasInternetConnection(ChooseLocationActivity.this)) {
            Toast.makeText(ChooseLocationActivity.this, ChooseLocationActivity.this.getResources().getString(R.string.connect_internet_alert_message), Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = VolleyRequest.getInstance(ChooseLocationActivity.this).getRequestQueue();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, WebserviceInfors.base_host_service + WebserviceInfors.loadDeliverPoints,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        ChooseLocationActivity.this.hideProgressDialog();

                        final ArrayList<DeliverPointModel> deliverPointList = new ArrayList<>() ;

                        try {
                            JSONObject jsResponse = new JSONObject(response);
                            if (jsResponse.getInt("code") == 999) {
                                deliverPointList.clear();
                                JSONArray jsonArray = jsResponse.getJSONArray("data");
                                Log.e(TAG, "onErrorResponse: " + jsonArray);

                                if (jsonArray != null) {
                                    if(jsonArray.length() > 0) {
                                        for (int i=0; i < jsonArray.length(); i++ ){
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            Gson gson = new Gson();
                                            DeliverPointModel deliverPointModel = gson.fromJson(jsonObject.toString(), DeliverPointModel.class);
                                            deliverPointList.add(deliverPointModel);
                                            Log.e(TAG, "deliverPointModel: " + deliverPointModel);
                                        }
                                    }
                                }
                            }
                            else {
//                                Toast.makeText(ChooseLocationActivity.this,jsResponse.getString("content"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ChooseLocationActivity.this.listViewAdapter.setData(deliverPointList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ChooseLocationActivity.this.hideProgressDialog();
                        Log.e(TAG, "onErrorResponse: " + error.toString());
//                        Toast.makeText(ChooseLocationActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//
//                params.put("status", Integer.toString(currentSelectedStatus));
//                params.put("key", category);
//                params.put("offer_id", user_id);
//                params.put("order", Integer.toString(order));
//                return params;
//            }

//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String>  params = new HashMap<>();
//                SharedPreferences sharedPreferences = Utils.getCommonSharepreference(ChooseLocationActivity.this);
//                params.put("x-access-token", sharedPreferences.getString(getResources().getString(R.string.login_token),""));
//                return params;
//            }

//        };
        queue.getCache().clear();
        queue.add(stringRequest);

    }

}
