package com.hiennd1412.ration.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hiennd1412.ration.R;

public class BaseActivity extends AppCompatActivity {

    private View loadingIndicator;
    private TextView currentLoadingTask;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
    }

    public void onBackButtonClicked() {
        this.finish();
    }

    public void showProgressDialog(){
        hideProgressDialog();
        progressdialog = ProgressDialog.show(BaseActivity.this, "",
                "Loading. Please wait...", true);
    }

    public void showProgressDialogWithText(String messageContent){
        hideProgressDialog();
        progressdialog = ProgressDialog.show(BaseActivity.this, "",
                "messageContent", true);
    }

    public void hideProgressDialog(){
//        Toast.makeText(BaseActivity.this,progressdialog., Toast.LENGTH_SHORT).show();
        if (progressdialog != null && progressdialog.isShowing()) {
            progressdialog.dismiss();
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
