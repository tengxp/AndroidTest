package com.test.unit;

import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.test.InstrumentationTestCase;
import android.util.Log;

public class MatrixTest extends InstrumentationTestCase {
	public static final String TAG = "UnitTest";
	
	public void printMatrix() {
		RectF screenRect = new RectF(0, 0, 200, 100);
		RectF bmpRect = new RectF(0, 0, 10, 10);
		
		Matrix oriMatrix = new Matrix();
		oriMatrix.setRectToRect(bmpRect, screenRect, ScaleToFit.CENTER);
		
		printLog(oriMatrix.toShortString());
	}
	
	private void printLog(String str) {
		Log.i(TAG, str);
	}
}
