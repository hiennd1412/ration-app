package com.hiennd1412.ration.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hiennd1412.ration.R;
import com.hiennd1412.ration.Utils.Utils;

public class AskLoginVerificationCodeActivity extends BaseActivity {

    static private final String TAG = AskLoginVerificationCodeActivity.class.getSimpleName();

    String mVerificationId;
    TextView tvVerificationCode;
    String choosenDeliverPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_login_verification_code);
        mVerificationId = getIntent().getStringExtra("mVerificationId");
        choosenDeliverPoint = getIntent().getStringExtra("deliverPointObject");
        tvVerificationCode = (EditText) findViewById(R.id.tv_verification_code);

    }

    public void onSaveButtonClicked(View v) {
        hideKeyboard();
        String verificationCode = tvVerificationCode.getText().toString().trim();
        if(verificationCode.equals("")) {
            Toast.makeText(this, "Bạn chưa nhập mã xác nhận!", Toast.LENGTH_LONG).show();
            return;
        }
        showProgressDialog();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        showProgressDialog();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            hideProgressDialog();
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            SharedPreferences.Editor sharePreferenceEditor = Utils.getCommonSharepreference(AskLoginVerificationCodeActivity.this).edit();
                            sharePreferenceEditor.putString(getResources().getString(R.string.choosen_location),choosenDeliverPoint);
                            sharePreferenceEditor.commit();
                            AskLoginVerificationCodeActivity.this.finish();
                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(AskLoginVerificationCodeActivity.this);
                            Intent localIntent = new Intent("ChooseLocationFinish");
                            localIntent.putExtra("ChooseLocation", "success");
                            localBroadcastManager.sendBroadcast(localIntent);

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            hideProgressDialog();
                            Toast.makeText(AskLoginVerificationCodeActivity.this, "Mã đăng nhập của bạn không đúng!", Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                            }
                        }
                    }
                });

    }
}
