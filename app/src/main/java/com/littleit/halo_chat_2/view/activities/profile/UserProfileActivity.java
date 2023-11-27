package com.littleit.halo_chat_2.view.activities.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.littleit.halo_chat_2.R;
import com.littleit.halo_chat_2.databinding.ActivityUserProfileBinding;

import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private ActivityUserProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);

        Intent intent = getIntent();
        String userName = intent.getStringExtra("userName");
        String receiverID = intent.getStringExtra("userID");
        String userProfile = intent.getStringExtra("userProfile");

        if (receiverID!=null){
            binding.toolbar.setTitle(userName);
            if (userProfile != null && userProfile.equals("")) {
                binding.imageProfile.setImageResource(R.drawable.icon_male);
            } else {
                Glide.with(this).load(userProfile).into(binding.imageProfile);
            }
        }
        initToolbar();
    }
    private void initToolbar(){
        binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    //@Override
    public boolean onCreateOptionMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //@Override
    public boolean onOptionItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}