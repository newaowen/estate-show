package com.warsong.app.estateshow.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.warsong.app.estateshow.helper.ActivityRecordHelper;

public class BaseActivity extends Activity {

	protected ActivityRecordHelper recordHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recordHelper = new ActivityRecordHelper(this);
	}

	protected void onResume() {
		super.onResume();
		recordHelper.onResume();
	}

	@Override
	public void startActivity(Intent intent) {
		recordHelper.startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		recordHelper.setResult();
		super.onBackPressed();
	}

	/**
	 * 注意在onResume之前执行
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		recordHelper.onActivityResult(requestCode, resultCode, data);
	}

}
