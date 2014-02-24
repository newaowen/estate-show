package com.warsong.app.estateshow.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.warsong.app.estateshow.misc.MenuKeyEnum;
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

	/**
	 * 在webView内拦截schema跳转
	 * 需要跳走时返回true,否则返回false
	 * @param act
	 * @param url
	 * @return
	 */
	public static boolean processUrlInWebView(Activity act, String url) {
		if (!StringUtil.isEmpty(url)) {
			//处理 土豆或youku视频直接调外部浏览器
			if (url.indexOf("youku.com") >= 0 || url.indexOf("tudou.com") >= 0) {
				act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
				return true;
			} else if (url.indexOf("back://") == 0) { //处理animate和back
				act.finish();
				return true;
			} else if (url.indexOf("animate://") == 0) {
				Bundle b = new Bundle();
				url = url.replaceFirst("animate://", "http://");
				b.putString("url", url);
				//b.putBoolean("hasTitle", false);
				JumpHelper.jump(act, MenuKeyEnum.KEY_HTMLVIEW, b);
				return true;
			} else if (url.indexOf("tel:") == 0) {
				String number = url.substring(4);
				number = number.replaceAll("[^0-9+-]+", "");
				Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
				act.startActivity(i);
				return true;
			} else {
				Bundle b = new Bundle();
				b.putString("url", url);
				//b.putBoolean("hasTitle", false);
				JumpHelper.jump(act, MenuKeyEnum.KEY_HTMLVIEW, b);
				return true;
			}
		} else {
			return false;
		}
	}
	
	public static void jump(Context context, String key, Bundle bundle) {
		Class<?> clazz = getClassFromKey(key);
		Log.i("JumpHelper", "jump key:" + key + ", to class: " + clazz.getCanonicalName());
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
		if (MenuKeyEnum.KEY_ONE.equals(key)) { //到首页
			clazz = MainActivity.class;
		} else if (MenuKeyEnum.KEY_NEWS.equals(key)) { //到新闻
			clazz = NewsActivity.class;
		} else if (MenuKeyEnum.KEY_PHOTOS.equals(key)) { //到图片浏览
			clazz = PhotoActivity.class;
		} else if (MenuKeyEnum.KEY_MAP.equals(key)) {
			clazz = MapActivity.class;
		} else if (MenuKeyEnum.KEY_HTMLVIEW.equals(key)) { //到htmlview
			clazz =  WebViewActivity.class;
		} else if (MenuKeyEnum.KEY_SETTING.equals(key)) {
			clazz = SettingActivity.class;
		} else if (MenuKeyEnum.KEY_UPDATE.equals(key)) {
			clazz = WebViewActivity.class;
		}
		
		return clazz;
	}
	
}
