package com.littleit.halo_chat_2.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.BuildConfig;
import com.littleit.halo_chat_2.R;
import com.littleit.halo_chat_2.databinding.ActivityMainBinding;
import com.littleit.halo_chat_2.menu.CallsFragment;
import com.littleit.halo_chat_2.menu.CameraFragment;
import com.littleit.halo_chat_2.menu.ChatsFragment;
import com.littleit.halo_chat_2.menu.StatusFragment;
import com.littleit.halo_chat_2.view.activities.contact.ContactsActivity;
import com.littleit.halo_chat_2.view.activities.settings.SettingsActivity;
import com.littleit.halo_chat_2.view.activities.status.AddStatusPicActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setUpWithViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        View tab1 = LayoutInflater.from(this).inflate(R.layout.custom_camera_tab, null);
        try {
            binding.tabLayout.getTabAt(0).setCustomView(tab1);
        } catch (Exception e){e.printStackTrace();}

        binding.viewPager.setCurrentItem(1);

        setSupportActionBar(binding.toolbar);

        binding.fabAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ContactsActivity.class));
            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeFabIcon(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setUpWithViewPager(ViewPager viewPager){
        MainActivity.SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new CameraFragment(),"");
        adapter.addFragment(new ChatsFragment(),"Chats");
        adapter.addFragment(new StatusFragment(),"Status");
        adapter.addFragment(new CallsFragment(),"Panggilan");
        viewPager.setAdapter(adapter);


    }

    //add this code
    private static class SectionsPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {super(manager);}

        @Override
        public Fragment getItem(int position) { return mFragmentList.get(position); }

        @Override
        public int getCount() { return mFragmentList.size(); }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {return mFragmentTitleList.get(position); }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_search){
            Toast.makeText(MainActivity.this, "Action Search", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_group_baru) {
            Toast.makeText(MainActivity.this, "Action Group Baru", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_siaran_baru) {
            Toast.makeText(MainActivity.this, "Action Siaran Baru", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_perangkat_tertaut) {
            Toast.makeText(MainActivity.this, "Action Perangkat Tertau", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_pesan_berbintang) {
            Toast.makeText(MainActivity.this, "Action Pesan Berbintang", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_setelan) {
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeFabIcon(final int index){
        binding.fabAction.hide();
        binding.btnAddStatus.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (index){
                    case 0 :
                        binding.fabAction.hide();
                        break;

                    case 1 :
                        binding.fabAction.show();
                        binding.fabAction.setImageDrawable(getDrawable(R.drawable.baseline_chat_24));
                        break;

                    case 2 :
                        binding.btnAddStatus.setVisibility(View.VISIBLE);
                        binding.fabAction.show();
                        binding.fabAction.setImageDrawable(getDrawable(R.drawable.baseline_camera_alt_24));
                        break;

                    case 3 :
                        binding.fabAction.show();
                        binding.fabAction.setImageDrawable(getDrawable(R.drawable.baseline_call_24));
                        break;

                }
                binding.fabAction.show();
            }
        },400);

        performOnClick(index);

    }

    private void performOnClick(int index) {
        binding.fabAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                if (index==1){
                    startActivity(new Intent(MainActivity.this, ContactsActivity.class));
                } else if (index==2){
//                    Toast.makeText(getApplicationContext(), "Kamera ...", Toast.LENGTH_LONG).show();
                    checkCameraPermission();
                } else {
                    Toast.makeText(getApplicationContext(), "Panggilan ...", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.btnAddStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Tambahkan Status ...", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkCameraPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    231);
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    232);
        }
        else {
            openCamera();
        }
    }

    public static Uri imageUri = null;
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMDD HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG" + timeStamp + ".jpg";

        try {
            File file = File.createTempFile("IMG_" + timeStamp, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            imageUri = FileProvider.getUriForFile(this, BuildConfig.DEBUG + ".provide" , file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra("listPhotoName", imageFileName);
            startActivityForResult(intent, 440);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 440
        && resultCode == RESULT_OK){
            //uploadToFirebase();
            if (imageUri!=null){
                startActivity(new Intent(MainActivity.this, AddStatusPicActivity.class)
                        .putExtra("image", imageUri));
            }
        }
    }

    private String getfileExtention(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}