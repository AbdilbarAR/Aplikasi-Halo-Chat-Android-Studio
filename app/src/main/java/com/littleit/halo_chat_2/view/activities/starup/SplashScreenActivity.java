package com.littleit.halo_chat_2.view.activities.starup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.littleit.halo_chat_2.R;
import com.littleit.halo_chat_2.view.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }
    }
}