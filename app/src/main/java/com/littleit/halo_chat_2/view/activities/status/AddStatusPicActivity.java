package com.littleit.halo_chat_2.view.activities.status;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.littleit.halo_chat_2.Model.StatusModel;
import com.littleit.halo_chat_2.R;
import com.littleit.halo_chat_2.databinding.ActivityAddStatusPicBinding;
import com.littleit.halo_chat_2.managers.ChatService;
import com.littleit.halo_chat_2.service.FirebaseService;
import com.littleit.halo_chat_2.view.MainActivity;

import java.util.UUID;

public class AddStatusPicActivity extends AppCompatActivity {
    private Uri imageUri;

    private ActivityAddStatusPicBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_status_pic);

        imageUri = MainActivity.imageUri;

        setInfo();
        initClick();
    }

    private void initClick() {
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FirebaseService(AddStatusPicActivity.this).uploadImageToFirebaseStorage(imageUri, new FirebaseService.OnCallBack() {
                    @Override
                    public void onUploadSuccess(String imageUri) {
                        StatusModel status = new StatusModel();
                        status.setId(UUID.randomUUID().toString());
                        status.setCreatedDate(new ChatService(AddStatusPicActivity.this).getCurrentDate());
                        status.setImageStatus(imageUri);
                        status.setUserID(FirebaseAuth.getInstance().getUid());
                        status.setViewCount("0");
                        status.setTextStatus(binding.edDescription.getText().toString());

                        new FirebaseService(AddStatusPicActivity.this).addNewStatus(status, new FirebaseService.OnAddNewStatusCallBack() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getApplicationContext(), "Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailed() {
                                Toast.makeText(getApplicationContext(), "Ada Sesuatu Yang Salah", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onUploadFailed(Exception e) {
                        Log.e("TAG", "onUploadFailed: ", e);
                    }
                });
            }
        });
    }

    private void setInfo() {
        Glide.with(this).load(imageUri).into(binding.imageView);
    }
}