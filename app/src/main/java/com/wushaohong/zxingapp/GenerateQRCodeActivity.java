package com.wushaohong.zxingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResultType;
import com.wushaohong.zxing.decode.QRDecode;
import com.wushaohong.zxing.encode.QREncode;

/**
 * author : wushaohong
 * e-mail : 576218811@qq.com
 * date   : 2019/08/28
 * desc   : 生成二维码页面 Activity
 * version: 1.0
 */
public class GenerateQRCodeActivity extends BaseDeCodeActivity {

    private ImageView mIvQRCode;
    private EditText mEtContent;
    private Button mBtGenerate;

    private Bitmap tempBitmap;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GenerateQRCodeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generateqrcode);
        
        initView();
    }

    private void initView() {
        
        mIvQRCode = findViewById(R.id.iv_qrcode);
        mEtContent = findViewById(R.id.et_content);
        mBtGenerate = findViewById(R.id.bt_generate_qrcode);
        
        mBtGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQRCode();
            }
        });

        mIvQRCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                analyzeQRCode();
                return false;
            }
        });
    }

    private void generateQRCode() {

        String contentStr = mEtContent.getText().toString();
        if (!TextUtils.isEmpty(contentStr)) {
            // Logo
            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            // 背景
            Bitmap bgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            Bitmap qrBitmap = new QREncode.Builder(this)
                    // 设置二维码颜色
                    .setColors(0xFF0094FF, 0xFFFED545, 0xFF5ACF00, 0xFFFF4081)
                    // 边框
                    .setMargin(0)
                    // 类型
                    .setParsedResultType(ParsedResultType.TEXT)
                    // 内容
                    .setContents(contentStr)
                    // 大小
                    .setSize(500)
                    // Logo，不能设置太大，不然影响二维码识别
                    .setLogoBitmap(logoBitmap, 30)
                    .build()
                    .encodeAsBitmap();

            tempBitmap = qrBitmap;
            mIvQRCode.setImageBitmap(qrBitmap);
        }
    }

    private void analyzeQRCode() {
        QRDecode.decodeQR(tempBitmap, this);
    }

    @Override
    void onScanResultActivity(Result result, ParsedResultType type, Bundle bundle) {
        Toast.makeText(this, result.getText(), Toast.LENGTH_LONG).show();
    }
}
