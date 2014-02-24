package com.warsong.app.estateshow.ui.widget;

import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * * not used, for experiment
 * *****************************************
 * 
 * This class serves as a WebView to be used in conjunction with a
 * VideoEnabledWebChromeClient. It makes possible: - To detect the HTML5 video
 * ended event so that the VideoEnabledWebChromeClient can exit full-screen. -
 * To detect other HTML5 video events to enter low profile UI.
 * 
 * Important notes: - Javascript is enabled by default and must not be disabled
 * with getSettings().setJavaScriptEnabled(false). - setWebChromeClient() must
 * be called before any loadData(), loadDataWithBaseURL() or loadUrl() method.
 * 
 * @author Cristian Perez (http://cpr.name)
 * 
 */
public class VideoWebView extends WebView {
	public class JavascriptInterface {
		@android.webkit.JavascriptInterface
		public void notifyVideoEnd() // Must match Javascript interface method
										// of VideoEnabledWebChromeClient
		{
			// This code is not executed in the UI thread, so we must force that
			// to happen
			new Handler(Looper.getMainLooper()).post(new Runnable() {
				@Override
				public void run() {
					if (videoEnabledWebChromeClient != null) {
						videoEnabledWebChromeClient.onHideCustomView();
					}
				}
			});
		}

		@android.webkit.JavascriptInterface
		public void notifyVideoLowProfile() // Must match Javascript interface
											// method of
											// VideoEnabledWebChromeClient
		{
			// This code is not executed in the UI thread, so we must force that
			// to happen
			new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
				@SuppressLint("NewApi") @Override
				public void run() {
					if (android.os.Build.VERSION.SDK_INT >= 14
							&& videoEnabledWebChromeClient.isVideoFullscreen()) {
						((Activity) getContext()).getWindow().getDecorView()
								.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
					}
				}
			}, 1000);
		}
	}

	private VideoWebChromeClient videoEnabledWebChromeClient;
	private boolean addedJavascriptInterface;

	public VideoWebView(Context context) {
		super(context);
		addedJavascriptInterface = false;
	}

	public VideoWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		addedJavascriptInterface = false;
	}

	public VideoWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		addedJavascriptInterface = false;
	}

	/**
	 * Pass only a VideoEnabledWebChromeClient instance.
	 */
	@Override
	public void setWebChromeClient(WebChromeClient client) {
		getSettings().setJavaScriptEnabled(true);

		if (client instanceof VideoWebChromeClient) {
			this.videoEnabledWebChromeClient = (VideoWebChromeClient) client;
		}

		super.setWebChromeClient(client);
	}

	@Override
	public void loadData(String data, String mimeType, String encoding) {
		addJavascriptInterface();
		super.loadData(data, mimeType, encoding);
	}

	@Override
	public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding,
			String historyUrl) {
		addJavascriptInterface();
		super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
	}

	@Override
	public void loadUrl(String url) {
		addJavascriptInterface();
		super.loadUrl(url);
	}

	@Override
	public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
		addJavascriptInterface();
		super.loadUrl(url, additionalHttpHeaders);
	}

	private void addJavascriptInterface() {
		if (!addedJavascriptInterface) {
			// Add javascript interface to be called when the video ends (must
			// be done before page load)
			addJavascriptInterface(new JavascriptInterface(), "_VideoEnabledWebView"); // Must
																						// match
																						// Javascript
																						// interface
																						// name
																						// of
																						// VideoEnabledWebChromeClient

			addedJavascriptInterface = true;
		}
	}

}
