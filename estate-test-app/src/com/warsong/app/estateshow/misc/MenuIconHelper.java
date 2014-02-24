package com.warsong.app.estateshow.misc;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.warsong.wb.estate.R;

/**
 * 将菜单对应的icon index转化为res id
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-19 下午3:05:41
 */
public class MenuIconHelper {

	/**
	 * 获取菜单的图标资源id
	 * 
	 * @param context
	 * @param index
	 * @return
	 */
	public static int getMenuIconResId(Context context, int index) {
		return getIconResId(context, index, "_80");
	}

	/**
	 * 获取九宫格的图标资源id
	 * 
	 * @param context
	 * @param index
	 * @return
	 */
	public static int getGridIconResId(Context context, int index) {
		return getIconResId(context, index, "_66");
	}

	private static int getIconResId(Context context, int index, String postfix) {
		String idStr = "i_" + index + postfix;

		int id = 0;
		try {
			Field field = R.drawable.class.getField(idStr);
			id = field.getInt(new R.drawable());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}

	public static Drawable getMenuIconDrawable(Context context, int id) {
		return context.getResources().getDrawable(getMenuIconResId(context, id));
	}
	
	public static Drawable getGridIconDrawable(Context context, int id) {
		return context.getResources().getDrawable(getGridIconResId(context, id));
	}


}
