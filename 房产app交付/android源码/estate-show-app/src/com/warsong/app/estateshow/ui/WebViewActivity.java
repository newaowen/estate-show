package com.warsong.app.estateshow.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.warsong.app.estateshow.R;
import com.warsong.app.estateshow.helper.JumpHelper;
import com.warsong.app.estateshow.util.GeneralUtil;
import com.warsong.app.estateshow.util.StringUtil;

/**
 * 网页查看界面
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-20 下午8:10:13
 */
public class WebViewActivity extends BaseSlideActivity {

	private final static String TAG = "WebViewActivity";

	private String title;
	// private boolean hasTitleFlag = true;
	private WebView webView;
	private String url;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContentView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.webview, null);
		setContentView(mContentView);

		Intent i = getIntent();
		if (i != null) {
			title = i.getStringExtra("title");
			if (StringUtil.isEmpty(title)) {
				title = getResources().getString(R.string.main_title);
			}
			// hasTitleFlag = i.getBooleanExtra("hasTitle", true);
			url = i.getStringExtra("url");
		}
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		// if (hasTitleFlag) {
		// setTitleText(title);
		// } else {
		// getTitleBar().setVisibility(View.GONE);
		// }
		setTitleText(title);
		initBothSideMenu();

		webView = (WebView) findViewById(R.id.webview);
		webView.setWebChromeClient(chromeClient);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return JumpHelper.processUrlInWebView(WebViewActivity.this, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				// 页面加载完后立即关闭菊花
				GeneralUtil.dismissLoading();
			}

			/**
			 * 特别：当客户端网络错误时，会显示个网络提示界面，同样会调用onPageStarted, 即将调用两次
			 */
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				Log.i(TAG, "onPageStarted: " + url);
				processLoading();
			}
		});

		// 开启js
		webView.getSettings().setJavaScriptEnabled(true);
		// 自动加载图片
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setPluginState(PluginState.ON);
		// webView.getSettings().setPluginsEnabled(true);
		webView.loadUrl(url);
	}

	private View mCustomView;
	private ViewGroup mContentView;
	private FrameLayout mCustomViewContainer;
	private boolean isShowCustomView = false;

	private WebChromeClient chromeClient = new WebChromeClient() {
		private WebChromeClient.CustomViewCallback mCustomViewCallback;

		FrameLayout.LayoutParams LayoutParameters = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			super.onShowCustomView(view, callback);
			if (view == null) {
				return;
			}

			isShowCustomView = true;
			try {
				if (mCustomViewCallback != null) {
					mCustomViewCallback.onCustomViewHidden();
					mCustomViewCallback = null;
					return;
				}

				mCustomViewContainer = new FrameLayout(WebViewActivity.this);
				mCustomViewContainer.setLayoutParams(LayoutParameters);
				mCustomViewContainer.setBackgroundResource(android.R.color.black);
				view.setLayoutParams(LayoutParameters);
				mCustomViewContainer.addView(view);
				mCustomView = view;
				mCustomViewCallback = callback;
				setContentView(mCustomViewContainer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onHideCustomView() {
			isShowCustomView = false;
			try {
				if (mCustomView == null) {
					return;
				} else {
					if (mCustomViewCallback != null) {
						mCustomViewCallback.onCustomViewHidden();
						mCustomViewCallback = null;
					}

					mCustomViewContainer.removeView(mCustomView);
					mCustomView = null;
					mCustomViewContainer = null;
					setContentView(mContentView);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	protected void onPause() {
		super.onPause();
		if (webView != null) {
			webView.onPause();
		}
	}

	protected void onResume() {
		super.onResume();
		if (webView != null) {
			webView.onResume();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.stopLoading();
		webView.destroy();
	}

	private void processLoading() {
		// 只在首次加载url时显示菊花，不要在onPageStarted中显示菊花
		GeneralUtil.showLoading(WebViewActivity.this);
		// ３秒后关闭菊花
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				GeneralUtil.dismissLoading();
			}
		}, 3000);
	}

	// To handle the back button key press
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (isShowCustomView && chromeClient != null) {
				chromeClient.onHideCustomView();
				return true;
			} else {
				if (webView.canGoBack()) {
					webView.goBack();
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
