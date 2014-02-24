package com.warsong.app.estateshow.helper;

import android.app.Activity;
import android.content.Intent;

import com.warsong.app.estateshow.rpc.RecordUseTimeRpc;

/**
 * activity辅助
 * 主要实现activity间跳转能识别是同一app内
 * 
 * @author newaowen@gmail.com
 * @date 2013-11-5 下午10:50:49
 */
public class ActivityRecordHelper {
	
	private Activity activity;
	
	public static final String REQUEST_CODE_KEY = "defaultRequestCode";
	public static final int DEFAULT_REQUEST_CODE = 100;
	public static final int DEFAULT_RESULT_CODE = 1000;
	
	private boolean innerIntentFlag = false;
	private boolean inAppResumeFlag = false;
	
	public ActivityRecordHelper(Activity act) {
		this.activity = act;
	}
	
	/**
	 * 包含启动和恢复
	 */
	public void onResume() {
		//是否需记录的标记
		boolean flag = true;
		innerIntentFlag = false;
		
		//启动时判断是否需记录
		Intent i = activity.getIntent();
		if (i == null) {
			flag = false;
		} else {
			if (inAppResumeFlag) {
				flag = false;
			} else {
				int v = i.getIntExtra(REQUEST_CODE_KEY, 0);
				if (v == DEFAULT_REQUEST_CODE) {
					innerIntentFlag = true;
					flag = false;
				}
			}
		}
		
		if (flag) {
			new RecordUseTimeRpc(activity).execute();
		}
	}
	
	public void startActivity(Intent i) {
		i.putExtra(REQUEST_CODE_KEY, DEFAULT_REQUEST_CODE);
		activity.startActivityForResult(i, DEFAULT_REQUEST_CODE);
	}
	
	public void setResult() {
		if (innerIntentFlag) {
			activity.setResult(DEFAULT_RESULT_CODE);
		}
	}
	
	/**
	 * 注意在onResume之前执行
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == DEFAULT_REQUEST_CODE) {
			if (resultCode == DEFAULT_RESULT_CODE) {
				inAppResumeFlag = true;
			} else {
				inAppResumeFlag = false;
			}
		}
	}
	
}
