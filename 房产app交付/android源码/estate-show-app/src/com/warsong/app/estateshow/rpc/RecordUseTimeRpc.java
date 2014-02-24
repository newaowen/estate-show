package com.warsong.app.estateshow.rpc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.misc.NetworkException;
import com.warsong.app.estateshow.model.SpmModel;
import com.warsong.app.estateshow.util.StringUtil;

/**
 * 记录用户手机号码
 * 
 * @author newaowen@gmail.com
 * @date 2013-11-5 下午10:24:53
 */
public class RecordUseTimeRpc extends AsyncTask<String, Object, Boolean> {
	
	private static final String TAG = "RecordUseTime";
	
	private Context context;
	
	public RecordUseTimeRpc(Context ctx) {
		this.context = ctx;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		boolean result = false;
		SpmModel spm = AppDataManager.getInstance().getSpmModel();
		if (spm != null) {
			String url = spm.getUser_open_times();
			if (!StringUtil.isEmpty(url)) {
				try {
					Log.i(TAG, "record use time to: " + url);
					RpcClient client = new RpcClient(context);
					String rpcResult = client.get(url);
					Log.i(TAG, "record result: " + rpcResult);
					result = true;
				} catch (NetworkException e) { //网络异常处理
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (result) {
			Log.i(TAG, "record use time no success");
		} else { //未获取到数据，应用无法正常使用，弹窗退出否?
			Log.i(TAG, "record use time no fail"); 
		}
		super.onPostExecute(result);
	}
}
 
