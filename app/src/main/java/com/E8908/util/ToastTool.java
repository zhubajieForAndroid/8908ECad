package com.E8908.util;

import android.content.Context;
import android.widget.Toast;

public class ToastTool {
	static private Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;
	private static String oldMsg;

	public static void setToast(Context context, String content) {

		// Toast.makeText(context, content, Toast.LENGTH_SHORT).show();

		if (toast == null) {
			toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
		} else {
			toast.setText(content);
		}
		toast.show();
	}

	

	public static void showToast(Context context, String text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
			toast.show();
			oneTime = System.currentTimeMillis();
			oldMsg = text;
		} else {
			twoTime = System.currentTimeMillis();
			if (text.equals(oldMsg)) {
				if (twoTime - oneTime > Toast.LENGTH_SHORT) {
					toast.setText(text);
					toast.show();
				}
			} else {
				oldMsg = text;
				toast.setText(text);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	public static void showToast(Context context, int resId) {
		showToast(context, context.getString(resId));
	}

}
