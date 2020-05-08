package com.hiennd1412.ration.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.facebook.imagepipeline.core.ImagePipelineExperiments;
import com.google.gson.Gson;
import com.hiennd1412.ration.CustomInterface.UpLoadImageSuccessHandler;
import com.hiennd1412.ration.Entity.AllocationModel;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;
import com.hiennd1412.ration.WebserviceGeneralManage.VolleyRequest;
import com.hiennd1412.ration.WebserviceGeneralManage.WebserviceInfors;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AllocationDetailsActivity extends Location_aBaseActivity {

    private static final String TAG = AllocationDetailsActivity.class.getSimpleName();

    AllocationModel theAllocationModel;
    TextView tvGuestPhoneNumber;
    TextView tvReceivedTime;
    ImageView deliverPointImage;
    TextView tvDeliverPointName;
    TextView tvDeliverPointLocation;
    TextView tvPackageCount;
    EditText tfNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocation_details);

        theAllocationModel = (new Gson()).fromJson(getIntent().getStringExtra("choosenAllocation"), AllocationModel.class);
        tvGuestPhoneNumber = (TextView) findViewById(R.id.tv_guest_number);
        tvReceivedTime = (TextView) findViewById(R.id.tv_received_time);
        deliverPointImage = (ImageView) findViewById(R.id.img_deliver_point);
        tvDeliverPointName = (TextView) findViewById(R.id.tv_deliver_point_name);
        tvDeliverPointLocation = (TextView) findViewById(R.id.tv_deliver_point_location);
        tvPackageCount = (TextView) findViewById(R.id.tv_package_count);
        tfNote = (EditText) findViewById(R.id.tf_note);
        selectedImage = (ImageView) findViewById(R.id.allowcation_image);

        if(theAllocationModel.guestPhoneNumber.equals("")) {
            tvGuestPhoneNumber.setText(theAllocationModel.guestIdentifyNumber);
        }
        else {
            tvGuestPhoneNumber.setText(theAllocationModel.guestPhoneNumber);
        }
        tvReceivedTime.setText(Utils.formatDate(theAllocationModel.allowcationTime));
        tvDeliverPointName.setText(theAllocationModel.deliverPoint.deliverPointName);
        tvDeliverPointLocation.setText(theAllocationModel.deliverPoint.location);
        tvPackageCount.setText("  " + theAllocationModel.packageCount + "  ");
        tfNote.setText(theAllocationModel.note);

        String imageLink = WebserviceInfors.base_host_service + WebserviceInfors.loadDeliverPointImage + theAllocationModel.deliverPoint.image;
        Glide.with(this)
                .load(imageLink)
                .placeholder(R.drawable.giving_to_charity_blog_size)
                .into(deliverPointImage);

        final String allowcationImageLink = WebserviceInfors.base_host_service + WebserviceInfors.loadAllocationImage + theAllocationModel.image;
        Glide.with(this)
                .load(allowcationImageLink)
                .placeholder(R.drawable.give_gift)
                .into(selectedImage);
    }

    public void onDownButtonClicked(View v) {
        if(theAllocationModel.packageCount > 1) {
            theAllocationModel.packageCount -= 1;
            tvPackageCount.setText("  " + theAllocationModel.packageCount + "  ");
        }
    }

    public void onUpButtonClicked(View v) {
        theAllocationModel.packageCount += 1;
        tvPackageCount.setText("  " + theAllocationModel.packageCount + "  ");
    }

    public void onSaveButtonClicked(View v) {
        showProgressDialog();

        if (attachImageUri != null) {
            uploadImage(attachImageUri.getPath(), WebserviceInfors.uploadAllocationImage, new UpLoadImageSuccessHandler() {
                @Override
                public void onUploadImageSuccess(String imageName) {
                    AllocationDetailsActivity.this.updateAllocation(imageName);
                }
            });
        }
        else {
            updateAllocation("");
        }
    }

    private void updateAllocation(String newImageName) {
        final String imageName = newImageName.equals("") ? theAllocationModel.image:newImageName;

        if(!Utils.hasInternetConnection(AllocationDetailsActivity.this)) {
            Toast.makeText(AllocationDetailsActivity.this, AllocationDetailsActivity.this.getResources().getString(R.string.connect_internet_alert_message), Toast.LENGTH_SHORT).show();
            return;
        }
        RequestQueue queue = VolleyRequest.getInstance(AllocationDetailsActivity.this).getRequestQueue();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, WebserviceInfors.base_host_service + WebserviceInfors.editAllowcateInfo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response);
                        AllocationDetailsActivity.this.hideProgressDialog();
                        try {
                            JSONObject jsResponse = new JSONObject(response);
                            if (jsResponse.getInt("code") == 999) {
                                Log.e(TAG, "success response: " + jsResponse);
                                Toast.makeText(AllocationDetailsActivity.this,"Edit Success!", Toast.LENGTH_SHORT).show();
                                LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(AllocationDetailsActivity.this);
                                Intent localIntent = new Intent("EditAllowcationFinish");
                                localIntent.putExtra("editAllocation", "success");
                                localBroadcastManager.sendBroadcast(localIntent);
                                AllocationDetailsActivity.this.finish();
                            }
                            else {
                                Toast.makeText(AllocationDetailsActivity.this,jsResponse.getString("message"), Toast.LENGTH_SHORT).show();
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
                        AllocationDetailsActivity.this.hideProgressDialog();
                        Toast.makeText(AllocationDetailsActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        )
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("_id", AllocationDetailsActivity.this.theAllocationModel._id);
                params.put("allowcationTime", Utils.formatDateToSendToServer(AllocationDetailsActivity.this.theAllocationModel.allowcationTime));
                params.put("packageCount", AllocationDetailsActivity.this.theAllocationModel.packageCount.toString());
                params.put("note", AllocationDetailsActivity.this.tfNote.getText().toString().trim());
                params.put("productInfo", AllocationDetailsActivity.this.theAllocationModel.productInfo);
                params.put("guestPhoneNumber", AllocationDetailsActivity.this.theAllocationModel.guestPhoneNumber);
                params.put("guestIdentifyNumber", AllocationDetailsActivity.this.theAllocationModel.guestIdentifyNumber);
                params.put("deliverPoint", AllocationDetailsActivity.this.theAllocationModel.deliverPoint._id);
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
