package com.littleit.halo_chat_2.view.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.littleit.halo_chat_2.R;
import com.littleit.halo_chat_2.databinding.ActivityPhoneLoginBinding;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity{

    private ActivityPhoneLoginBinding binding;

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ProgressDialog progressDialog;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_login);


        //
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            startActivity(new Intent(this, SetUserInfoActivity.class));
        }

        progressDialog = new ProgressDialog(this);
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btnNext.getText().toString().equals("Next")) {

                    String phone = "+" + binding.edCodeCountry.getText().toString() + binding.edPhone.getText().toString();
                    startPhoneNumberVerification(phone);
                } else {
                    progressDialog.setMessage("Memproses ...");
                    progressDialog.show();
                    verifyPhoneNumberWithCode(mVerificationId, binding.edCode.getText().toString());
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d("TAG", "onVerificationCompleted: Complete");
                signInwithPhoneAuthCredential(phoneAuthCredential);
                progressDialog.dismiss();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.d("TAG", "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token){
                Log.d("TAG", "onCodeSent:" + verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

                binding.btnNext.setText("Confirm");
                binding.edCode.setVisibility(View.VISIBLE);
                binding.edCodeCountry.setEnabled(false);
                binding.edPhone.setEnabled(false);
                progressDialog.dismiss();
            }
        };
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        progressDialog.setMessage("Send Code to: " + phoneNumber);
        progressDialog.show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        );

    }

    private void verifyPhoneNumberWithCode(String verificationId,String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        signInwithPhoneAuthCredential(credential);
    }

    private void signInwithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Log.d("TAG", "signInwithCredential: success");

                        FirebaseUser user = task.getResult().getUser();
                        startActivity(new Intent(PhoneLoginActivity.this, SetUserInfoActivity.class));

                        //if (user != null) {
                        //    String userID = user.getUid();
                        //    Users users = new Users(userID,
                        //            "",
                        //            user.getPhoneNumber(),
                        //            "",
                        //            "",
                        //            "",
                        //            "",
                        //            "",
                        //            "",
                        //            "");

                        //    firestore.collection("Users").document("UserInfo").collection(userID)
                        //            .add(users).addOnSuccessListener(documentReference -> {
                        //                startActivity(new Intent(PhoneLoginActivity.this, SetUserInfoActivity.class));
                        //            });
                        //} else {
                        //    Toast.makeText(getApplicationContext(), "Something Error", Toast.LENGTH_SHORT).show();
                        //}

                    } else {
                        progressDialog.dismiss();
                        Log.w("TAG", "signInwithCredential: failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Log.d("TAG", "onComplete: Error Code");
                        }
                    }
                });
    }
}