package com.warsong.app.estateshow.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {

	public static NetworkInfo getActiveNetworkInfo(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connectivityManager.getActiveNetworkInfo();
	}

	/**
	 * 网络类型
	 */
	public static int getNetType(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
		if (ni == null)
			return -1;
		return ni.getType();
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean isNetworkAvailable = false;
		ConnectivityManager connectivity = (ConnectivityManager) (context
				.getSystemService(Context.CONNECTIVITY_SERVICE));
		NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
		if (networkInfos == null)
			return false;

		for (NetworkInfo itemInfo : networkInfos) {
			if (itemInfo != null) {
				if (itemInfo.getState() == NetworkInfo.State.CONNECTED
						|| itemInfo.getState() == NetworkInfo.State.CONNECTING) {
					isNetworkAvailable = true;
					break;
				}
			}
		}

		return isNetworkAvailable;
	}
}
