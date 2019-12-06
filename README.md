# Android二维码扫描/生成，仿支付宝扫描，亮度检测等

简书：https://www.jianshu.com/p/ed79eeed2b00

二维码扫描，识别，生成是日常开发比较常见的需求，以前公司的项目做过扫描功能，简单地把ZXing的核心代码（jar包）集成到项目当中，虽然现实了扫描识别的功能，但是功能扩展性差，移植性也不好。因此，决定在业余时间基于网上大神的开源代码重新封装ZXing，精简代码，模仿支付宝微信实现摄像头亮度检测打开闪光灯功能，以及扫描远处二维码放大功能。
// TODO 手势放大缩小功能待业余时间实现
![正常亮度](https://upload-images.jianshu.io/upload_images/16821601-08dcb9f6c9823c67.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![低光环境](https://upload-images.jianshu.io/upload_images/16821601-608dea4634fbbb43.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![生成二维码，长按图片识别](https://upload-images.jianshu.io/upload_images/16821601-3f80a0a3981de796.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



代码环节
![引入zxing模块](https://upload-images.jianshu.io/upload_images/16821601-7e28dfb1330435fb.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


自定义扫描基类 BaseScannerActivity
```
package com.wushaohong.zxingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ISBNParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ParsedResultType;
import com.google.zxing.client.result.ProductParsedResult;
import com.google.zxing.client.result.TextParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import com.wushaohong.zxing.OnScannerCompletionListener;
import com.wushaohong.zxing.common.Scanner;
import com.wushaohong.zxing.result.AddressBookResult;
import com.wushaohong.zxing.result.ISBNResult;
import com.wushaohong.zxing.result.ProductResult;
import com.wushaohong.zxing.result.URIResult;

/**
 * author : wushaohong
 * e-mail : 576218811@qq.com
 * date   : 2019/08/26
 * desc   : 摄像头扫描基类
 * version: 1.0
 */
public abstract class BaseScannerActivity extends AppCompatActivity implements
        OnScannerCompletionListener {

    private static final String TAG = BaseScannerActivity.class.getSimpleName();

    @Override
    public void onScannerCompletion(final Result rawResult, ParsedResult parsedResult, Bitmap
            barcode) {

        if (rawResult == null) {
            Toast.makeText(this, "未发现二维码", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 震动
        vibrate();

        final Bundle bundle = new Bundle();
        // 解析结果类型
        final ParsedResultType type = parsedResult.getType();
        Log.i(TAG, "ParsedResultType: " + type);
        switch (type) {
            case ADDRESSBOOK:
                AddressBookParsedResult addressBook = (AddressBookParsedResult) parsedResult;
                bundle.putSerializable(Scanner.Scan.RESULT, new AddressBookResult(addressBook));
                break;
            case PRODUCT:
                ProductParsedResult product = (ProductParsedResult) parsedResult;
                Log.i(TAG, "productID: " + product.getProductID());
                bundle.putSerializable(Scanner.Scan.RESULT, new ProductResult(product));
                break;
            case ISBN:
                ISBNParsedResult isbn = (ISBNParsedResult) parsedResult;
                Log.i(TAG, "isbn: " + isbn.getISBN());
                bundle.putSerializable(Scanner.Scan.RESULT, new ISBNResult(isbn));
                break;
            case URI:
                URIParsedResult uri = (URIParsedResult) parsedResult;
                Log.i(TAG, "uri: " + uri.getURI());
                bundle.putSerializable(Scanner.Scan.RESULT, new URIResult(uri));
                break;
            case TEXT:
                TextParsedResult textParsedResult = (TextParsedResult) parsedResult;
                bundle.putString(Scanner.Scan.RESULT, textParsedResult.getText());
                break;
            case GEO:
                break;
            case TEL:
                break;
            case SMS:
                break;
            default:
                break;
        }

        onScanResultActivity(rawResult, type, bundle);
    }

    /**
     * 低亮度检测
     * @param isWeakLight 是否低亮度
     */
    @Override
    public void onCheckWeakLight(boolean isWeakLight) {

    }

    /**
     * 子类实现，根据 ParsedResultType 处理业务
     * @param result
     * @param type
     * @param bundle
     */
    abstract void onScanResultActivity(Result result, ParsedResultType type, Bundle bundle);

    /**
     * 震动
     */
    private void vibrate() {
        // 震动（需要增加权限）
        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(150);
        }
    }
}
```

使用

扫码布局添加控件
```
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- 扫码控件 -->
    <com.wushaohong.zxing.ScannerView
        android:id="@+id/sv_scan"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">
    </com.wushaohong.zxing.ScannerView>

    <!-- 标题栏 -->
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:gravity="center_vertical"
        android:layout_marginTop="20dp"
        android:paddingStart="20dp"
        android:paddingEnd="20dp">

        <ImageView
            android:id="@+id/iv_back"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_back" />

        <View
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:layout_weight="1" />

        <TextView
            android:id="@+id/tv_album"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="相册"
            android:textSize="16sp"
            android:textColor="@android:color/white" />

    </LinearLayout>

    <!-- 弱光提示，手电筒打开 -->
    <LinearLayout
        android:id="@+id/ll_weak_light"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:gravity="center_horizontal"
        android:layout_centerInParent="true"
        android:visibility="gone">

        <ImageView
            android:id="@+id/iv_flash_lamp"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_flashlight_close" />

        <TextView
            android:id="@+id/tv_weak_light_tips"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="5dp"
            android:text="轻点照亮"
            android:textColor="@android:color/white" />

    </LinearLayout>

</RelativeLayout>
```

代码设置属性
```
        // 设置扫描回调，this是继承BaseScannerActivity或者单独实现OnScannerCompletionListener接口的类
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
```

相册选择识别二维码，长按图片识别
```
          /*
          读取到相片的bitmap，图片bitmap
          this是继承BaseScannerActivity或者单独实现OnScannerCompletionListener接口的类
          */
          QRDecode.decodeQR(bitmap, this);
```

生成二维码，根据文本生成二维码图片，bitmap
```
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
```

Note：
1、扫码二维码记得添加相机权限，如果需要震动也需要添加震动权限，相册选择的读权限手机权限。
2、低光环境判断标准可以通过修改zxing里面的解码帧数据的分析代码修改标准值。
