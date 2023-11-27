package com.littleit.halo_chat_2.view.activities.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;

import com.bumptech.glide.Glide;
import com.littleit.halo_chat_2.Model.ChatList;
import com.littleit.halo_chat_2.R;
import com.littleit.halo_chat_2.common.Common;
import com.littleit.halo_chat_2.view.activities.chats.ChatsActivity;
import com.littleit.halo_chat_2.view.activities.display.ViewImageActivity;
import com.littleit.halo_chat_2.view.activities.profile.ProfileActivity;
import com.littleit.halo_chat_2.view.activities.profile.UserProfileActivity;

public class DialogViewUser {
    private Context context;

    public DialogViewUser(Context context, ChatList chatList){
        this.context = context;
        initialize(chatList);
    }

    public void initialize(ChatList chatList){
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        dialog.setContentView(R.layout.dialog_view_user);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);

        ImageButton btnChat, btnCall, btnVideoCall, btnInfo;
        ImageView profile;
        TextView username;

        btnChat = dialog.findViewById(R.id.btn_chat);
        btnCall = dialog.findViewById(R.id.btn_call);
        btnVideoCall = dialog.findViewById(R.id.btn_video);
        btnInfo = dialog.findViewById(R.id.btn_info);
        profile = dialog.findViewById(R.id.image_profile);
        username = dialog.findViewById(R.id.tv_username);

        username.setText(chatList.getUserName());
        Glide.with(context).load(chatList.getUrlProfile()).into(profile);

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatsActivity.class)
                        .putExtra("userID", chatList.getUserID())
                        .putExtra("userName", chatList.getUserName())
                        .putExtra("userProfile", chatList.getUrlProfile()));
                dialog.dismiss();
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Call Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Video Call Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, UserProfileActivity.class)
                        .putExtra("userID", chatList.getUserID())
                        .putExtra("userProfile", chatList.getUrlProfile())
                        .putExtra("userName", chatList.getUserName()));
                dialog.dismiss();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.invalidate();
                Drawable dr = profile.getDrawable();

                if (dr instanceof BitmapDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                    Common.IMAGE_BITMAP = bitmap;
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, profile, "image");
                    Intent intent = new Intent(context, ViewImageActivity.class);
                    context.startActivity(intent, activityOptionsCompat.toBundle());
                } else {
                    // Handle the case where the image is not a BitmapDrawable (e.g., not loaded by Glide).
                }
            }
        });

        dialog.show();
    }

}
