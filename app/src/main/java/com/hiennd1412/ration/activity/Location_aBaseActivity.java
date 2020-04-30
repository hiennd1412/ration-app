package com.hiennd1412.ration.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.theartofdev.edmodo.cropper.CropImage;
import com.hiennd1412.ration.CustomInterface.UpLoadImageSuccessHandler;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;
import com.hiennd1412.ration.WebserviceGeneralManage.VolleyRequest;
import com.hiennd1412.ration.WebserviceGeneralManage.WebserviceInfors;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Location_aBaseActivity extends BaseActivity {

    private static final String TAG =  "AddNewLocationActivity";
    private final static int RESULT_SELECT_IMAGE = 100;
    private final static int RESULT_CAMERA_PHOTO_TAKEN = 101;

    EditText tfDeliverPointName;
    EditText tfDeliverPointLocation;
    EditText tfDeliverPointPhonenumber;
    TextView tfEndDate;
    TextView tfStartDate;
    TextView tfOpenTime;
    TextView tfCloseTime;
    ImageView locationImage;

    Date startDate;
    Date endDate;
    protected Uri attachImageUri = null;
    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onTakePhotoButtonClicked(View v){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Chọn nguồn ảnh");
        String[] pictureDialogItems = {
                "Thư viện ảnh",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                startGallery();
                                break;
                            case 1:
                                startCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void startGallery() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2000);
                return;
            }
        }
        Intent cameraIntent = new Intent(Intent.ACTION_GET_CONTENT);
        cameraIntent.setType("image/*");
        if (cameraIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(cameraIntent, RESULT_SELECT_IMAGE);
        }
    }

    private void startCamera() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 2001);
                return;
            }
        }
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_CAMERA_PHOTO_TAKEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case RESULT_SELECT_IMAGE:
            case RESULT_CAMERA_PHOTO_TAKEN:
                if (resultCode == this.RESULT_OK) {
                    Uri imageUri = data.getData();
                    CropImage.activity(imageUri)
                            .start(this);
                }
                break;

            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == this.RESULT_OK) {

                    attachImageUri = result.getUri();
                    this.locationImage.setImageURI(attachImageUri);
//                    File compressedImage = compressImage(new File(attachImageUri.getPath()));
//                    this.imgQuestion.setImageURI(Uri.fromFile(compressedImage));

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                }
                break;
        }
    }

    protected void uploadImage(final String imagePath, final UpLoadImageSuccessHandler handler) {
        RequestQueue queue = VolleyRequest.getInstance(this).getRequestQueue();
        queue.getCache().clear();

        File compressedImage = compressImage(new File(imagePath));

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, WebserviceInfors.base_host_service + WebserviceInfors.uploadDeliverPointImage,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String returnImageName = jObj.getString("data");
                            handler.onUploadImageSuccess(returnImageName);
                        } catch (JSONException e) {
                            Location_aBaseActivity.this.hideProgressDialog();
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(Location_aBaseActivity.this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Location_aBaseActivity.this.hideProgressDialog();
                        Toast.makeText(Location_aBaseActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
//                SharedPreferences sharedPreferences = Utils.getCommonSharepreference(getActivity());
//                params.put("x-access-token", sharedPreferences.getString(getResources().getString(R.string.login_token),""));
                return params;
            }
        };

        smr.addFile("image", compressedImage.getPath());
        queue.add(smr);

    }

    private File compressImage(File fileToCompress) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 1;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileToCompress);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 2048;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }
//        Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(fileToCompress);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File destFile = new File(fileToCompress.getParent(), "img_" + new Date().toString() + ".jpeg");
        try {
            FileOutputStream out = new FileOutputStream(destFile);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return destFile;
    }
}
