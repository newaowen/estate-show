package com.warsong.app.estateshow.helper;

import android.util.Log;

import com.warsong.app.estateshow.cache.DiskCache;
import com.warsong.app.estateshow.util.StringUtil;

public class CacheHelper {

	private final static String TAG = "CacheHelper";

	public final static String GLOBAL_DATA_KEY = "global_data_key";

	public static String getGlobalData() {
		byte[] d = get(GLOBAL_DATA_KEY);
		if (d != null) {
			return new String(d);
		} else {
			return null;
		}
	}

	public static void putGlobalData(String data) {
		put(GLOBAL_DATA_KEY, data);
	}

	public static byte[] get(String key) {
		if (StringUtil.isEmpty(key)) {
			return null;
		}
		byte[] d = null;
		DiskCache dc = new DiskCache();
		try {
			dc.open();
			d = dc.get(key);
			dc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return d;
	}

	public static void put(String key, String data) {
		if (!StringUtil.isEmpty(data)) {
			put(key, data.getBytes());
		}
	}

	public static void put(String key, byte[] data) {
		if (data == null) {
			Log.i(TAG, "put cache " + key + "data is empty");
			return;
		}

		DiskCache dc = new DiskCache();
		try {
			dc.open();
			dc.put(key, data);
			dc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
