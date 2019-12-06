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
 * desc   : 解析图片基类
 * version: 1.0
 */
public abstract class BaseDeCodeActivity extends AppCompatActivity implements OnScannerCompletionListener {

    private static String TAG = BaseDeCodeActivity.class.getSimpleName();

    @Override
    public void onScannerCompletion(final Result rawResult, ParsedResult parsedResult, Bitmap
            barcode) {

        if (rawResult == null) {
            Toast.makeText(this, "未发现二维码", Toast.LENGTH_SHORT).show();
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
