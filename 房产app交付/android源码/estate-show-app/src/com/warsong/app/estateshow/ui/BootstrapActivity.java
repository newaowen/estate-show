package com.warsong.app.estateshow.ui;

import java.util.Set;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.warsong.app.estateshow.R;
import com.warsong.app.estateshow.helper.CacheHelper;
import com.warsong.app.estateshow.helper.JumpHelper;
import com.warsong.app.estateshow.info.AppInfo;
import com.warsong.app.estateshow.info.DeviceInfo;
import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.misc.NetworkException;
import com.warsong.app.estateshow.model.ConfigModel;
import com.warsong.app.estateshow.model.GlobalModel;
import com.warsong.app.estateshow.rpc.RecordPhoneNoRpc;
import com.warsong.app.estateshow.rpc.RpcClient;
import com.warsong.app.estateshow.util.GeneralUtil;
import com.warsong.app.estateshow.util.StringUtil;

/**
 * 引导界面
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-18 下午8:06:05
 */
public class BootstrapActivity extends Activity {

	private static final String TAG = "BootstrapActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bootstrap);

		init();
		loadGlobalData();
	}

	private void init() {
		//初始化全局对象
		AppDataManager.createInstance(this);
		DeviceInfo.createInstance(this);
		AppInfo.createInstance(this);
	}

	/**
	 * TODO分离出单独的service 从服务端获取全局配置数据
	 */
	private void loadGlobalData() {
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
				RpcClient client = new RpcClient(BootstrapActivity.this);
				result = client.get(params[0]);
				g = GlobalModel.buildFromJson(result);
			} catch (NetworkException e) { //网络异常处理
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
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
				//成功后延迟一段时间后再跳转到首页
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						onInitGlobalDataOk();
					}
				}, 500);
			} else { //未获取到数据，应用无法正常使用，弹窗退出否?
				if (GeneralUtil.isActivityValid(BootstrapActivity.this)) {
					GeneralUtil.alert(BootstrapActivity.this, "", "网络异常, 请稍后再试",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									finish();
								}
							}, null);
				}
			}
			super.onPostExecute(result);
		}
	}

	private void onInitGlobalDataOk() {
		//初始化push
		initJPush(); 
		
		//首次使用收集用户手机号上传
		String phoneNo = GeneralUtil.getLocalPhoneNo(this);
		if (!StringUtil.isEmpty(phoneNo)) {
			new RecordPhoneNoRpc(this).execute(phoneNo);
		}
		
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
	
	private void initJPush() {
		boolean debug = AppInfo.getInstance().isDebuggable();
		JPushInterface.setDebugMode(debug);
		JPushInterface.init(this);
		setJPushAlias(); 
	}
	
	private TagAliasCallback tagAliasCallback = new TagAliasCallback() {
		
		@Override
		public void gotResult(int arg0, String arg1, Set<String> arg2) {
			Log.d(TAG, "设置别名回调：" + arg1);
			// 0设置成功
			if (0 != arg0) {
				Log.d(TAG, "别名设置失败重新设置");
				//重新设置
				setJPushAlias();
			}
		}
	};
	
	private void setJPushAlias() {
		ConfigModel config = AppDataManager.getInstance().getConfig();
		if (config != null) {
			String alias = config.getBuilding();
			if (!StringUtil.isEmpty(alias)) {
				JPushInterface.setAlias(getApplicationContext(), alias, tagAliasCallback);
			}
		}
	}
	
}
