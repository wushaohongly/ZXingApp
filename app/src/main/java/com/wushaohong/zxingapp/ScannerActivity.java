package com.wushaohong.zxingapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResultType;
import com.wushaohong.zxing.ScannerView;
import com.wushaohong.zxing.camera.open.CameraFacing;
import com.wushaohong.zxing.common.Scanner;
import com.wushaohong.zxing.decode.QRDecode;

import java.io.FileNotFoundException;

/**
 * author : wushaohong
 * e-mail : 576218811@qq.com
 * date   : 2019/08/28
 * desc   : 摄像头扫描二维码页面 Activity
 * version: 1.0
 */
public class ScannerActivity extends BaseScannerActivity {

    private static final int CHOOSE_PICTURE_REQUEST_CODE = 10;

    private ImageView mIvBack;
    private TextView mTvAlbum;
    /**
     * 扫描控件
     */
    private ScannerView mScannerView;

    /**
     * 闪光灯提示布局
     */
    private LinearLayout mLlWeak;
    private ImageView mIvFlashLamp;
    private TextView mTvWeakTips;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ScannerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        // 屏幕常亮
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_scanner);
        initView();
    }

    private void initView() {

        mIvBack = findViewById(R.id.iv_back);
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mTvAlbum = findViewById(R.id.tv_album);
        mTvAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        // 扫描框
        mScannerView = findViewById(R.id.sv_scan);
        // 设置扫描回调
        mScannerView.setOnScannerCompletionListener(this);
        // 设置扫描成功的声音
        mScannerView.setMediaResId(R.raw.beep);
        // 扫描框下方文字
        mScannerView.setDrawText("放入框内，自动扫描", true);
        // 文字颜色
        mScannerView.setDrawTextColor(0xFF26CEFF);
        mScannerView.setScanMode(Scanner.ScanMode.QR_CODE_MODE);
        mScannerView.isScanFullScreen(false);
        // 边框颜色
        mScannerView.setLaserFrameBoundColor(0xFF26CEFF);
        // 支付宝网格
        mScannerView.setLaserGridLineResId(R.mipmap.zfb_grid_scan_line);

        // 后置摄像头
        mScannerView.setCameraFacing(CameraFacing.BACK);
        // 扫描框与屏幕上方距离
        mScannerView.setLaserFrameTopMargin(150);
        // 设置4角长度
        mScannerView.setLaserFrameCornerLength(25);
        // 设置扫描线高度
        mScannerView.setLaserLineHeight(10);
        // 扫描框四个边角宽度
        mScannerView.setLaserFrameCornerWidth(3);

        // 设置扫描结果放大
//        mScannerView.setResultZoom(true);
        // 设置扫描结果放大后是否重置
//        mScannerView.setResultZoomReset(true);
        // 设置扫描结果方法后，重置毫秒数
//        mScannerView.setResultZoomResetDelay(2000);

        mLlWeak = findViewById(R.id.ll_weak_light);
        mIvFlashLamp = findViewById(R.id.iv_flash_lamp);
        mTvWeakTips = findViewById(R.id.tv_weak_light_tips);

        mLlWeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlFlashLamp();
            }
        });
    }

    @Override
    protected void onResume() {
        mScannerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mScannerView.onPause();
        mLlWeak.setVisibility(View.GONE);
        super.onPause();
    }

    @Override
    void onScanResultActivity(Result result, ParsedResultType type, Bundle bundle) {
        Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();
        // 2000毫秒后进入下一次扫描
//        mScannerView.restartPreviewAfterDelay(2000);
    }

    @Override
    public void onCheckWeakLight(boolean isWeakLight) {
        super.onCheckWeakLight(isWeakLight);
        boolean isOpen = mScannerView.isLightStatus();
        // 如果是开着闪光灯，将不会自动隐藏
        if (isOpen) {
            mLlWeak.setVisibility(View.VISIBLE);
        } else {
            mLlWeak.setVisibility(isWeakLight ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (Activity.RESULT_OK == resultCode) {
            switch (requestCode) {
                case CHOOSE_PICTURE_REQUEST_CODE:
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            ContentResolver cr = this.getContentResolver();
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                                QRDecode.decodeQR(bitmap, this);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * 选择图片
     */
    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_PICTURE_REQUEST_CODE);
    }

    /**
     * 控制闪光灯
     */
    private void controlFlashLamp() {
        boolean isOpen = mScannerView.isLightStatus();
        isOpen = !isOpen;
        mIvFlashLamp.setImageDrawable(getDrawable(isOpen ?
                R.drawable.ic_flashlight_open : R.drawable.ic_flashlight_close));
        mScannerView.toggleLight(isOpen);
    }
}
