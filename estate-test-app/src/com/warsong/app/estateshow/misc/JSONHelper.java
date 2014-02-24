package com.warsong.app.estateshow.misc;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

/**
 * 对Gson的基本封装
 * 
 * @deprecated
 * @author newaowen@gmail.com
 * @date 2013-10-19 下午3:35:37
 */
public class JSONHelper {

	public static <T> Object fromJson(String json, Class<T> clazz) {
		Gson j = new Gson();
		return j.fromJson(json, clazz);
	}
	
	public static String toJson(Object src) {
		Gson j = new Gson();
		return j.toJson(src);
	}
	
	/**
	 * 从jsonobject中解析出某个key对应的java对象
	 * 
	 * @param obj
	 * @param key
	 * @param clazz
	 * @return
	 */
	public static <T> Object fromJSONObject(JSONObject obj, String key, Class<T> clazz) {
		Object target = null;
		try {
			JSONObject targetObj = obj.getJSONObject(key);
			if (targetObj != null) {
				target = JSONHelper.fromJson(targetObj.toString(), clazz);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return target;
	}
 
}
