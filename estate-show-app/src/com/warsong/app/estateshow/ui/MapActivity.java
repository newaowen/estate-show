package com.warsong.app.estateshow.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.warsong.app.estateshow.R;
import com.warsong.app.estateshow.model.LocationModel;
import com.warsong.app.estateshow.util.GeneralUtil;

/**
 * 地图界面 使用google mapv3定位到指定经纬度
 * mock:lat, lon: 31.23691,121.50109 
 * @author newaowen@gmail.com
 */
public class MapActivity extends BaseSlideActivity {
	//
	private static final String MAP_URL = "file:///android_asset/map.html";
	private WebView webView;

	// 由intent带过来的数据
	private String title;
	private LocationModel location;

	private AlertDialog naviDialog;
	private String[] naviTitles;
	private Intent[] naviIntents;
	

	@Override
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);

		location = new LocationModel();
		Intent intent = getIntent();
		if (intent != null) {
			title = intent.getStringExtra("title");// 标题栏文本
			location.setTitleShow(intent.getStringExtra("titleShow"));
			location.setSubTitleShow(intent.getStringExtra("subTitleShow"));
			location.setLatitude(intent.getStringExtra("latitude"));
			location.setLongitude(intent.getStringExtra("longitude"));
		}

		// 初始化导航数据
		naviTitles = new String[2];
		naviIntents = new Intent[2];
		naviTitles[0] = "谷歌地图导航";
		naviIntents[0] = getGoogleNaviIntent();
		naviTitles[1] = "高德地图导航";
		naviIntents[1] = getAMapNaviIntent();

		initView();
	}

	private void initView() {
		setTitleText(title);
		initBothSideMenu();

		setupWebView();

		ImageView right = (ImageView) findViewById(R.id.right_btn);
		right.setImageDrawable(getResources().getDrawable(R.drawable.navigate));
		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//只能打开google导航
				// String origin = "google.navigation:q=" +
				// location.getLatitude() + "," + location.getLongitude();
//				String origin = "http://maps.google.com/maps?daddr=" + location.getLatitude() + ","
//						+ location.getLongitude();
//				Uri uri = Uri.parse(origin);
//				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//				startActivity(intent);
				onNavigate();
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void setupWebView() {
		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				GeneralUtil.dismissLoading();
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}
		});

		// 设置优先使用缓存防止浪费流量
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.addJavascriptInterface(new JavaScriptInterface(), "android");
		
		webView.loadUrl(MAP_URL);
		processLoading();
	}
	
	private void processLoading() {
		// 只在首次加载url时显示菊花，不要在onPageStarted中显示菊花
		GeneralUtil.showLoading(MapActivity.this);
		// ３秒后关闭菊花
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				GeneralUtil.dismissLoading();
			}
		}, 3000);
	}

	/**
	 * Sets up the interface for getting access to Latitude and Longitude data
	 * from device
	 **/
	@SuppressWarnings("unused")
	private class JavaScriptInterface {
		@JavascriptInterface
		public double getLatitude() {
			return Double.parseDouble(location.getLatitude());
		}

		@JavascriptInterface
		public double getLongitude() {
			return Double.parseDouble(location.getLongitude());
		}

		@JavascriptInterface
		public String getTitle() {
			return location.getTitleShow();
		}

		@JavascriptInterface
		public String getSubTitle() {
			return location.getSubTitleShow();
		}
	}

	private void onNavigate() {
		//判断google和高德的intent是否存在
		//如都存在弹出选择对话框，如只存在一个则直接跳转
		//不存在时弹弱提示
		boolean flagOne = GeneralUtil.isIntentExist(MapActivity.this, naviIntents[0]);
		boolean flagTwo = GeneralUtil.isIntentExist(MapActivity.this, naviIntents[1]);
		if (flagOne && flagTwo) {
			showNaviDialog();
		} else if (flagOne && !flagTwo) {
			startActivity(naviIntents[0]);
		} else if (!flagOne && flagTwo) {
			startActivity(naviIntents[1]);
		} else {
			GeneralUtil.toast(MapActivity.this, "系统内未安装导航应用");
		}
	}

	private void showNaviDialog() {
		if (naviDialog == null) {
			naviDialog = createNaviDialog();
		}
		naviDialog.show();
	}

	private AlertDialog createNaviDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择导航").setItems(naviTitles, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try {
					startActivity(naviIntents[which]);
				} catch (android.content.ActivityNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		return builder.create();
	}

	private Intent getGoogleNaviIntent() {
		// google导航 (后面增加label,次级label?)
		Uri uri = Uri.parse("google.navigation:ll=" + location.getLatitude() + ","
				+ location.getLongitude());
		Intent i = new Intent(Intent.ACTION_VIEW, uri);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return i;
	}

	private Intent getAMapNaviIntent() {
		Uri uri = Uri.parse("androidamap://navi?sourceApplication=estate-show" //poiname="
				//+ location.getTitleShow()
				+ "&lat=" + location.getLatitude() + "&lon="
				+ location.getLongitude() + "&dev=1&style=2");
		Intent i = new Intent(Intent.ACTION_VIEW, uri);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		return i;
	}

}
