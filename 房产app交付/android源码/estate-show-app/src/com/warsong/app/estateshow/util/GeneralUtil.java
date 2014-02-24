package com.warsong.app.estateshow.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.warsong.app.estateshow.ui.widget.GenericProgressDialog;

public class GeneralUtil {

	private static AlertDialog loadingDialog;

	public static boolean isIntentExist(Context context, Intent intent) {
		List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
		if (list.size() > 0) {
			return true;
		}
		return false;
	}

	public static void alert(Activity context, String title, String content,
			DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener cancelListener) {
		if (!isActivityValid(context)) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(content);
		if (positiveListener != null) {
			builder.setPositiveButton("确定", positiveListener);
		}
		if (cancelListener != null) {
			builder.setNegativeButton("取消", cancelListener);
		}

		builder.show();
	}

	public static void toast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	public static void showLoading(Activity context) {
		showLoading(context, "", null);
	}

	public static void showLoading(Activity context, String msg,
			DialogInterface.OnCancelListener cancelListener) {
		if (!isActivityValid(context)) {
			return;
		}

		if (loadingDialog != null && loadingDialog.isShowing()) {
			return;
		}

		loadingDialog = new GenericProgressDialog(context);
		loadingDialog.setMessage(msg);
		((GenericProgressDialog) loadingDialog).setProgressVisiable(true);
		loadingDialog.setCancelable(true);
		loadingDialog.setOnCancelListener(cancelListener);
		loadingDialog.setCanceledOnTouchOutside(false);
		loadingDialog.show();
	}

	public static void dismissLoading() {
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dpToPx(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int pxToDp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 避免在更新背景时忽略掉padding
	 * 
	 * @param v
	 * @param resId
	 */
	public static void updateViewBackground(View v, int resId) {
		int pL = v.getPaddingLeft();
		int pT = v.getPaddingTop();
		int pR = v.getPaddingRight();
		int pB = v.getPaddingBottom();

		v.setBackgroundResource(resId);
		v.setPadding(pL, pT, pR, pB);
	}

	public static byte[] streamToByte(InputStream is) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

	public static boolean isActivityValid(Activity act) {
		return !(act == null || act.isFinishing());
	}

	/**
	 * 获取本机手机号（注意：只有sim卡中保存了才有效)
	 * 
	 * @param ctx
	 * @return
	 */
	public static String getLocalPhoneNo(Context ctx) {
		TelephonyManager phoneMgr = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		return phoneMgr.getLine1Number();
	}
	
	public static String getConfigFromManifest(Context ctx, String key) {
		String result = "";
		try { 
		    ApplicationInfo ai = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA); 
		    Bundle bundle = ai.metaData; 
		    result = bundle.getString(key); 
		} catch (NameNotFoundException e) { 
		    Log.e("getValueFromManifest", "Failed to load meta-data, NameNotFound: " + e.getMessage()); 
		} catch (NullPointerException e) { 
		    Log.e("getValueFromManifest", "Failed to load meta-data, NullPointer: " + e.getMessage());          
		} 
		
		return result;
	}
	
	public static boolean isDebug(Context ctx) {
		return (ctx.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
	}
	
	 
	public static String addUrlLastBS(String url) {
		if (!"/".equals(url.substring(url.length() - 1))) {
			url += "/";
		}
		return url;
	}
	
}
