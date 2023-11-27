package com.littleit.halo_chat_2.service;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.littleit.halo_chat_2.Model.StatusModel;

public class FirebaseService {

    private Context context;

    public FirebaseService(Context context) {
        this.context = context;
    }

    public void uploadImageToFirebaseStorage(Uri uri, OnCallBack onCallBack){
        StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("ImagesChats/" + System.currentTimeMillis() + "." + getfileExtention(uri));
        riversRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task < Uri > uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uri;

                final String sdownload_uri = String.valueOf(downloadUri);

                onCallBack.onUploadSuccess(sdownload_uri);
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onCallBack.onUploadFailed(e);
            }
        });
    }

    private String getfileExtention(Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public interface OnCallBack{
        void onUploadSuccess(String imageUri);
        void onUploadFailed(Exception e);
    }

    public void addNewStatus(StatusModel statusModel, OnAddNewStatusCallBack onAddNewStatusCallBack){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Status Daily").document(statusModel.getId()).set(statusModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                onAddNewStatusCallBack.onSuccess();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onAddNewStatusCallBack.onFailed();
                    }
                });
    }

    public interface OnAddNewStatusCallBack{
        void onSuccess();
        void onFailed();
    }
}
