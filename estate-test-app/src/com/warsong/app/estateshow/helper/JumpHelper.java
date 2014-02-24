package com.warsong.app.estateshow.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.warsong.app.estateshow.ui.AppUpdateActivity;
import com.warsong.app.estateshow.ui.MainActivity;
import com.warsong.app.estateshow.ui.MapActivity;
import com.warsong.app.estateshow.ui.NewsActivity;
import com.warsong.app.estateshow.ui.PhotoActivity;
import com.warsong.app.estateshow.ui.SettingActivity;
import com.warsong.app.estateshow.ui.WebViewActivity;
import com.warsong.app.estateshow.util.StringUtil;

/**
 * 跳转辅助
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-20 下午8:06:34
 */
public class JumpHelper {

	public static final String KEY_ONE = "One";
	public static final String KEY_NEWS = "News";
	public static final String KEY_PHOTOS = "Photos";
	public static final String KEY_SETTING = "Setting";
	public static final String KEY_MAP = "Map";
	public static final String KEY_HTMLVIEW = "HTMLView";
	public static final String KEY_UPDATE = "update";
	
	/**
	 * 在webView内拦截schema跳转
	 * 需要跳走时返回true,否则返回false
	 * @param act
	 * @param url
	 * @return
	 */
	public static boolean processUrlInWebView(Activity act, String url) {
		if (!StringUtil.isEmpty(url)) {
			//处理animate和back
			if (url.indexOf("back://") == 0) {
				act.finish();
				return true;
			} else if (url.indexOf("animate://") == 0) {
				Bundle b = new Bundle();
				url = url.replaceFirst("animate://", "http://");
				b.putString("url", url);
				JumpHelper.jump(act, JumpHelper.KEY_HTMLVIEW, b);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public static void jump(Context context, String key, Bundle bundle) {
		Class<?> clazz = getClassFromKey(key);
		if (clazz != null) {
			Intent i = new Intent();
			if (bundle != null) {
				i.putExtras(bundle);
			}
			i.setClass(context, clazz);
			context.startActivity(i);
		}
	}
	
	public static Class<?> getClassFromKey(String key) {
		Class<?> clazz = null;
		if (KEY_ONE.equals(key)) { //到首页
			clazz = MainActivity.class;
		} else if (KEY_NEWS.equals(key)) { //到新闻
			clazz = NewsActivity.class;
		} else if (KEY_PHOTOS.equals(key)) { //到图片浏览
			clazz = PhotoActivity.class;
		} else if (KEY_MAP.equals(key)) {
			clazz = MapActivity.class;
		} else if (KEY_HTMLVIEW.equals(key)) { //到htmlview
			clazz =  WebViewActivity.class;
		} else if (KEY_SETTING.equals(key)) {
			clazz =  SettingActivity.class;
		} else if (KEY_UPDATE.equals(key)) {
			clazz =  AppUpdateActivity.class;
		}
		
		return clazz;
	}
	
}
