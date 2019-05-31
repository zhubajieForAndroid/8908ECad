package com.E8908.util;

import android.content.Context;

public class GetSize {
	static int SIZEWORD;
	static int LRCHEIGHT;
	public int hvWidth;

	static public int getwordsize(Context context) {
		int width = context.getResources().getDisplayMetrics().widthPixels;
		if (width >= 720)
			SIZEWORD = 35;
		if (width >= 500 && width < 720)
			SIZEWORD = 30;
		if (width >= 480 && width < 500)
			SIZEWORD = 23;
		if (width < 480)
			SIZEWORD = 19;
		return SIZEWORD;
	}

	static public int getsongtitlesize(Context context) {
		int width = context.getResources().getDisplayMetrics().widthPixels;

		return (int) (width / 2.5);
	}

	static public int getlrcHeight(Context context) {
		if (context == null) {
			return 5;
		}
		int height = context.getResources().getDisplayMetrics().heightPixels;
		if (height >= 1280)
			LRCHEIGHT = 10;
		if (height >= 900 && height < 1280)
			LRCHEIGHT = 5;
		if (height >= 800 && height < 900)
			LRCHEIGHT = 1;
		if (height < 800)
			LRCHEIGHT = 0;
		return LRCHEIGHT;
	}

	static public int getscreenWidth(Context context) {
		int width = context.getResources().getDisplayMetrics().widthPixels;

		return width;
	}

	static public int getscreenHeight(Context context) {
		int heigh = context.getResources().getDisplayMetrics().heightPixels;

		return heigh;
	}

	static public int getdestric(Context context)

	{
		return context.getResources().getDisplayMetrics().densityDpi;
	}

	/**
	 * ����ֻ�ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * ����ֻ�ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

//	public static View getViewSize(final View view) {
//		
//		ViewTreeObserver vto = view.getViewTreeObserver();
//
//		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//
//			@Override
//			public void onGlobalLayout() {
//				// TODO Auto-generated method stub
//				view.getViewTreeObserver()
//						.removeGlobalOnLayoutListener(this);
//				tempView=view;
////				int height2 = view.getMeasuredHeight();
////
////				int width2 = view.getMeasuredWidth();
////				System.out.println("width2:" + width2);
//				
//				return tempView;
//			}
//
//		});
//	}
}
