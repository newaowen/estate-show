package com.warsong.app.estateshow.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定制ViewPager
 * １．阻止多点触摸时触发翻页
 * 
 * @author newaowen@gmail.com
 * @date 2013-11-6 上午9:08:22
 */
public class CustomViewPager extends ViewPager {

	public CustomViewPager(Context context) {
		super(context);
	}
	
	public CustomViewPager(Context context, AttributeSet as) {
		super(context, as);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getPointerCount() == 1) {
			boolean result = false;
			try{
				result = super.onTouchEvent(event);
				} catch(IllegalArgumentException e) {
					e.printStackTrace();
				}
			return result;
		} else {
			return true;
		}
	}

}
