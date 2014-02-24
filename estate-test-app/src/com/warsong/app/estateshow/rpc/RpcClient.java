package com.warsong.app.estateshow.rpc;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class RpcClient {
	
	public static int TIMEOUT = 10;

	public String get(String url) throws Exception {
		String result = "";
		// HttpGet连接对象
		HttpGet httpRequest = new HttpGet(url);
		// 取得HttpClient对象
		DefaultHttpClient httpClient = new DefaultHttpClient();
		final HttpParams httpParameters = httpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT * 1000);
		
		// 请求HttpClient，取得HttpResponse
		HttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpRequest);
			// 请求成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串
				result = EntityUtils.toString(httpResponse.getEntity());
			} else {
				// 其他服务端异常
				throw new Exception("server is in trouble");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return result;
	}

}
