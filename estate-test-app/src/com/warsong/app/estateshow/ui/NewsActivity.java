package com.warsong.app.estateshow.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.warsong.app.estateshow.helper.JumpHelper;
import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.misc.MenuKeyEnum;
import com.warsong.app.estateshow.model.MenuItemModel;
import com.warsong.app.estateshow.util.StringUtil;
import com.warsong.wb.estate.R;

/**
 * 新闻查看list界面 　 (本身是一个webview,　但需监听内部href，跳转到新闻详情界面)
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-20 下午8:10:13
 */
public class NewsActivity extends BaseSlideActivity {

	private String title;
	private String url;
	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);

		MenuItemModel menu = AppDataManager.getInstance().getMenuByKey(MenuKeyEnum.KEY_NEWS);
		Intent i = getIntent();
		if (i != null) {
			title = i.getStringExtra("title");
			if (StringUtil.isEmpty(title)) {
				title = menu.getTitle();
			}
			
			url = i.getStringExtra("url");
			if (StringUtil.isEmpty(url)) {
				url = menu.getUrl();
			}
		}

		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		setTitleText(title);
		initBothSideMenu();

		webView = (WebView) findViewById(R.id.webview);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return JumpHelper.processUrlInWebView(NewsActivity.this, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.i("newsAct", "onPageFinished");
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
		});

		//开启js
		webView.getSettings().setJavaScriptEnabled(true);
		//自动加载图片
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.loadUrl(url);
	}

	// To handle the back button key press
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
 
}
