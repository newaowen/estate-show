package com.warsong.app.estateshow.rpc;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.misc.NetworkException;
import com.warsong.app.estateshow.model.SpmModel;
import com.warsong.app.estateshow.util.GeneralUtil;
import com.warsong.app.estateshow.util.StringUtil;

/**
 * 记录用户手机号码
 * 
 * @author newaowen@gmail.com
 * @date 2013-11-5 下午10:24:53
 */
public class RecordPhoneNoRpc extends AsyncTask<String, Object, Boolean> {
	
	private static final String TAG = "RecordPhoneNoRpc";
	
	private Context context;
	
	public RecordPhoneNoRpc(Context ctx) {
		this.context = ctx;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		boolean result = false;
		
		SpmModel spm = AppDataManager.getInstance().getSpmModel();
		if (spm != null) {
			String url = spm.getUser_cell_number();
			if (!StringUtil.isEmpty(url)) {
				try {
					RpcClient client = new RpcClient(context);
					url = GeneralUtil.addUrlLastBS(url);
					String lastUrl = url + params[0] + "/";
					Log.i(TAG, "record phone no : " + lastUrl);
					String rpcResult = client.get(lastUrl);
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
			Log.i(TAG, "record phone no success");
		} else { //未获取到数据，应用无法正常使用，弹窗退出否?
			Log.i(TAG, "record phone no fail"); 
		}
		super.onPostExecute(result);
	}
}
 
