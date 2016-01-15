package com.czm.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by caizhiming on 2016/1/14.
 */
public class DensityUtil {
	/**
	 * 根据手机的分辨率 dp 转成 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率px(像素)  转成  dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	public static float getScreenDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static int getStatusBarHeight(Context ctx) {
		Class<?> c = null;

		Object obj = null;

		Field field = null;

		int x = 0, sbar = 0;

		try {

			c = Class.forName("com.android.internal.R$dimen");

			obj = c.newInstance();

			field = c.getField("status_bar_height");

			x = Integer.parseInt(field.get(obj).toString());

			sbar = ctx.getResources().getDimensionPixelSize(x);

		} catch (Exception e1) {

			e1.printStackTrace();

		}

		return sbar;
	}
	
	public static int getScreenHeightWithoutTitlebar(Context ctx) {
		int[] screenWidthAndHeight = getScreenWidthAndHeight(ctx);
		return screenWidthAndHeight[1] -getStatusBarHeight(ctx)- dip2px(ctx, 48);
	}

	public static int[] getScreenWidthAndHeight(Context ctx) {
		WindowManager mWm = (WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE);

		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		mWm.getDefaultDisplay().getMetrics(dm);

		int screenWidth = dm.widthPixels;

		int screenHeigh = dm.heightPixels;

		return new int[] { screenWidth, screenHeigh };
	}
	public static int[] getLocation(View v) {
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		return location;
	}
	public static int getRealHeight(Activity con) {
		int dpi = 0;
		Display display = con.getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		@SuppressWarnings("rawtypes")
		Class c;
		try {
			c = Class.forName("android.view.Display");
			@SuppressWarnings("unchecked")
			Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
			method.invoke(display, dm);
			dpi = dm.heightPixels;
			// Log.e("Real size", dm.widthPixels + "*" + dm.heightPixels);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dpi;
	}
}
