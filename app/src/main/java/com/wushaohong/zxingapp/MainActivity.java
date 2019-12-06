package com.wushaohong.zxingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * author : wushaohong
 * e-mail : 576218811@qq.com
 * date   : 2019/08/26
 * desc   : 主页面
 * version: 1.0
 */
public class MainActivity extends AppCompatActivity {

    private Button btScan;
    private Button btGenerate;

    private static final int PERMISSION_REQUEST_CODE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        btScan = findViewById(R.id.bt_scan);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                    } else {
                        startScan();
                    }
                } else {
                    startScan();
                }

            }
        });

        btGenerate = findViewById(R.id.bt_generate);
        btGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenerateQRCodeActivity.startActivity(MainActivity.this);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PERMISSION_REQUEST_CODE == requestCode) {
            if (Manifest.permission.CAMERA.equals(permissions[0])) {
                if (PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                    startScan();
                } else {
                    Toast.makeText(MainActivity.this, "没有相机权限", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void startScan() {
        ScannerActivity.startActivity(this);
    }

}
