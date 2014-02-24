package com.warsong.app.estateshow.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.warsong.app.estateshow.helper.CacheHelper;
import com.warsong.app.estateshow.helper.JumpHelper;
import com.warsong.app.estateshow.info.DeviceInfo;
import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.model.GlobalModel;
import com.warsong.app.estateshow.rpc.RpcClient;
import com.warsong.app.estateshow.util.GeneralUtil;
import com.warsong.wb.estate.R;

/**
 * 引导界面
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-18 下午8:06:05
 */
public class BootstrapActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bootstrap);

		initGlobalHelper();
		initGlobalData();
	}

	private void initGlobalHelper() {
		DeviceInfo.createInstance(this);
	}

	/**
	 * TODO分离出单独的service 从服务端获取全局配置数据
	 */
	private void initGlobalData() {
		new GetGlobalDataTask().execute(AppDataManager.getInstance().getGlobalDataUrl());
	}

	// 第一个参数：输入，第二个参数 进度
	private class GetGlobalDataTask extends AsyncTask<String, Object, Boolean> {

		public GetGlobalDataTask() {

		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean fromRemote = true;
			String result = null;
			GlobalModel g = null;
			try {
				RpcClient client = new RpcClient();
				result = client.get(params[0]);
				g = GlobalModel.buildFromJson(result);
			} catch (Exception e) { //网络异常处理

			}
		 
			if (g == null) {//从缓存读取
				String str = CacheHelper.getGlobalData();
				g = GlobalModel.buildFromJson(str);
				fromRemote = false;
			}
			if (g == null) {
				return false;
			} else {
				// 已获取有效的全局数据
				AppDataManager.getInstance().setGlobalModel(g);
				// TODO 保存如本地缓存
				if (fromRemote) {
					CacheHelper.putGlobalData(result);
				}
				return true;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				// 成功后延迟一段时间后再跳转到首页
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						onInitGlobalDataOk();
					}
				}, 500);
			} else { // 未获取到数据，应用无法正常使用，弹窗退出否？
				GeneralUtil.alert(BootstrapActivity.this, "", "网络异常, 请稍后再试",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								finish();
							}
						}, null);
			}
			super.onPostExecute(result);
		}
	}

	private void onInitGlobalDataOk() {
		Intent intent = getIntent();
		if (intent != null) {
			Bundle b = intent.getExtras();
			if (b != null) {
				String key = b.getString("target");
				JumpHelper.jump(this, key, b);
				return;
			}
		}
		intent = new Intent(BootstrapActivity.this, MainActivity.class);
		startActivity(intent);
	}
}
