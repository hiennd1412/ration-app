package com.hiennd1412.ration.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.CallLog;
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

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.gson.Gson;
import com.hiennd1412.ration.Entity.DeliverPointModel;
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
import java.util.concurrent.TimeUnit;

public class ChooseLocationActivity extends BaseActivity {

    private static final String TAG = ChooseLocationActivity.class.getSimpleName();

    ListView listView;
    ListAdapter_DeliverPointList listViewAdapter;
    DeliverPointModel selectedDeliverPoint;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("ChooseLocation");
            if(message.equals("success")) {
                ChooseLocationActivity.this.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        setupListview();
        getDeliverPointList();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("ChooseLocationFinish"));
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
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

    public void doVerifyUserByPhoneNumber(final DeliverPointModel aDeliverPointModel) {

        selectedDeliverPoint = aDeliverPointModel;

        showProgressDialog();

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

//                         signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                }
                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
//                         mVerificationId = verificationId;
//                         mResendToken = token;

                Gson gson = new Gson();
                String chooseDeliverPoint = gson.toJson(aDeliverPointModel);

                Intent anIntent = new Intent(ChooseLocationActivity.this, AskLoginVerificationCodeActivity.class);
                anIntent.putExtra("mVerificationId",verificationId);
                anIntent.putExtra("deliverPointObject", chooseDeliverPoint);
                ChooseLocationActivity.this.startActivity(anIntent);

                // ...
            }
        };

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                aDeliverPointModel.phonenumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                (Activity) ChooseLocationActivity.this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

}
