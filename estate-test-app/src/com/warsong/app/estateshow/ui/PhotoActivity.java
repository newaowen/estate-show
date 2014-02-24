package com.warsong.app.estateshow.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.warsong.app.estateshow.helper.ShareHelper;
import com.warsong.app.estateshow.model.PhotoModel;
import com.warsong.app.estateshow.ui.adapter.PhotoViewPagerAdapter;
import com.warsong.wb.estate.R;

public class PhotoActivity extends BaseSlideActivity {

	private ViewPager viewPager;

	private List<PhotoModel> photos;
	private List<String> urls;
	private List<String> photoTitles;
	private String title;
	private String currentImgUrl;

	private TextView titleTV;
	private TextView pageTV;
	private ImageView shareBtn;

	private UMSocialService shareService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.photo);

		// 从intent获取数据
		Intent i = getIntent();
		if (i != null) {
			urls = i.getStringArrayListExtra("urls");
			photoTitles = i.getStringArrayListExtra("photoTitles");
			title = i.getStringExtra("title");
		}

		shareService = ShareHelper.init(PhotoActivity.this);
		initView();
	}

	private void initView() {
		setTitleText(title);
		initBothSideMenu();

		photos = new ArrayList<PhotoModel>();
		for (int i = 0; i < urls.size(); i++) {
			photos.add(new PhotoModel(urls.get(i), title));
		}

		titleTV = (TextView) findViewById(R.id.title);
		pageTV = (TextView) findViewById(R.id.page_index);
		onImageSelcted(0);

		shareBtn = (ImageView) findViewById(R.id.share);
		shareBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// appName + title + desc
				String content = getResources().getString(R.string.main_title) + "," + title + ","
						+ (String) titleTV.getText();
				ShareHelper.open(PhotoActivity.this, shareService, content, currentImgUrl);
			}
		});

		viewPager = (ViewPager) findViewById(R.id.view_pager);
		PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter(this, photos);
		viewPager.setAdapter(adapter);
		// view pager离屏换存
		viewPager.setOffscreenPageLimit(5);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(final int arg0) {
				onImageSelcted(arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	private void onImageSelcted(int i) {
		pageTV.setText((i + 1) + "/" + photos.size());
		titleTV.setText(photoTitles.get(i));
		currentImgUrl = photos.get(i).url;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 使用SSO必须添加，指定获取授权信息的回调页面，并传给SDK进行处理
		UMSsoHandler ssoHandler = shareService.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.d("", "#### ssoHandler.authorizeCallBack");
		}
	}

}
