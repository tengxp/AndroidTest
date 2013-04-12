
package com.test.unit;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.test.ActivityTestCase;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MatrixTest extends ActivityTestCase {
    public static final String TAG = "UnitTest";
    public static final int SCREEN_WIDTH = 1920;
    public static final int SCREEN_HEIGHT = 1080;

    public void printMatrix() {
        RectF screenRect = new RectF(0, 0, 200, 100);
        RectF bmpRect = new RectF(0, 0, 10, 10);

        Matrix oriMatrix = new Matrix();
        oriMatrix.setRectToRect(bmpRect, screenRect, ScaleToFit.CENTER);

        pLog(oriMatrix.toShortString());
    }

    public void printDecode() throws IOException {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        Resources res = getInstrumentation().getContext().getResources();
        BitmapFactory.decodeResource(res, R.drawable.big18144x4032, opts);
        pLog("outWidth : " + opts.outWidth + "--- outHeight : " + opts.outHeight);

        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(res.openRawResource(R.drawable.big18144x4032), 512 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bis.close();
        }

        opts.inJustDecodeBounds = false;
        opts.inSampleSize = getInSmapleSize(opts, 1920 * 1080);
        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.big18144x4032, opts);
        if (bitmap != null) {
            pLog("inSampleSize : " + opts.inSampleSize + "---width : " + bitmap.getWidth()
                    + "---height : " + bitmap.getHeight());
        } else {
            Log.e(TAG, "bitmap is NULL.");
        }
    }

    private void pLog(String str) {
        Log.i(TAG, str);
    }
    
    private class Decoder {
        private BitmapFactory.Options mOptions;
        private int expectSize ;
        
        public void decodeResource(Context context, int id) {
            
        }
        
        public void decodeFile(String path) {
            
        }
        
        public int getExpectSize() {
            return expectSize;
        }

        public void setExpectSize(int expectSize) {
            this.expectSize = expectSize;
        }

        private void decodeBitmap(InputStream is, boolean isJustDecodeBounds) {
            if (mOptions == null) {
                mOptions = new Options();
            }
            mOptions.inJustDecodeBounds = isJustDecodeBounds;
            mOptions.inSampleSize = isJustDecodeBounds ? 1 : getInSmapleSize();
            BitmapFactory.decodeStream(is, null, mOptions);
        }

        // 18144 / 1920 = 9.45
        // 4032 / 1080 = 3.73333
        // 18144 * 4032 / 1920 / 1080 = 35.28
        // 35.28 开平方 = 5.9396
        // inSampleSize : 6
        private int getInSmapleSize() {
            int inSampleSize = 1;
            int pixels = mOptions.outHeight * mOptions.outWidth;
            if (pixels > getExpectSize()) {
                // Math.round(long) 四舍五入
                long size = Math.round(Math.sqrt(pixels / getExpectSize())); 
                inSampleSize = (int) ((int) size < size ? size + 1 : size);
            }
            return inSampleSize;
        }
    }
}
