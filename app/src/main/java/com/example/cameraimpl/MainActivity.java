package com.example.cameraimpl;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    private CameraManager mCameraManager;
    private final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private ImageView mImageView;
    private TextureView textureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textureView = findViewById(R.id.textureView);
        mImageView = findViewById(R.id.ivTemp);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermission();
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager() ) {
                Log.d("sgq", "onCreate: 有权限");
            } else {
                Log.d("sgq", "onCreate: 去申请权限");
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1111);
            }
        }

        if ( ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 11);
            Toast.makeText(this, "没有相机权限！", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("sgq", "checkPermission: 有读写权限");

            if (mCameraManager == null || !mCameraManager.cameraState) {
                init();
            }
        }
    }

    private void init() {
        textureView.setVisibility(View.VISIBLE);
        mCameraManager = new CameraManager(this, textureView, mImageView);

        findViewById(R.id.btnTakePic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraManager.takePicture();
            }
        });
        findViewById(R.id.ivExchange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCameraManager.exchangeCamera();
            }
        });
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BannerActivity.class));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermission = true;
        for (int code : grantResults) {
            if (code != 0) {
                hasPermission = false;
                break;
            }
        }
        if (requestCode == 11 && hasPermission) {
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraManager.releaseCamera();
        mCameraManager.releaseThread();
    }
}