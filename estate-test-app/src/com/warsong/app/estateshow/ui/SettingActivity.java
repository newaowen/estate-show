package com.warsong.app.estateshow.ui;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umeng.socialize.controller.UMSocialService;
import com.warsong.app.estateshow.helper.JumpHelper;
import com.warsong.app.estateshow.helper.ShareHelper;
import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.model.MenuItemModel;
import com.warsong.app.estateshow.model.SettingItemModel;
import com.warsong.app.estateshow.util.GeneralUtil;
import com.warsong.wb.estate.R;

/**
 * 设置界面
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-29 下午10:28:16
 */
public class SettingActivity extends BaseSlideActivity {

	private List<SettingItemModel> settings;
	private ViewGroup settingBox;

	private UMSocialService socialService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting);
		socialService = ShareHelper.init(SettingActivity.this);
		initView();
	}

	private void initView() {
		setTitleText(getResources().getString(R.string.setting));
		initBothSideMenu();

		MenuItemModel menuItem = AppDataManager.getInstance().getMenuByKey("Setting");
		if (menuItem == null) {
			return;
		}

		settingBox = (ViewGroup) findViewById(R.id.setting_box);
		settings = menuItem.getCells();
		if (settings != null) {
			for (int i = 0; i < settings.size(); i++) {
				addSettingItem(settings.get(i), i);
			}
		}
//	 
		// updateBox = findViewById(R.id.app_update_box);
		// estateShareBox = findViewById(R.id.estate_share_box);
		// barcodeShareBox = findViewById(R.id.barcode_share_box);
		// aboutBox = findViewById(R.id.about_box);
		//
		// updateBox.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Intent i = new Intent(SettingActivity.this, AppUpdateActivity.class);
		// startActivity(i);
		// }
		// });
		//
		// estateShareBox.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		//
		// barcodeShareBox.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });
		//
		// aboutBox.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// }
		// });

	}

	private void addSettingItem(final SettingItemModel settingItem, int index) {
		View v = LayoutInflater.from(this).inflate(R.layout.setting_item, settingBox, false);
		settingBox.addView(v); 
		//android bug, 设置背景后会自动清空padding
		//先保存padding，然后再恢复
		int resId = R.drawable.table_top_selector;
		if (index == settings.size() - 1) {
			resId = R.drawable.table_bottom_selector;
		} else if (index > 0){
			resId = R.drawable.table_center_selector;
		}
		GeneralUtil.updateViewBackground(v, resId);

		TextView tv = (TextView) v.findViewById(R.id.label);
		tv.setText(settingItem.getTitle());

		final String key = settingItem.getKey();
		v.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("Share".equals(key)) {
					ShareHelper.open(SettingActivity.this, socialService, settingItem.getContent(),
							null);
				} else {
					Bundle b = new Bundle();
					b.putString("url", settingItem.getUrl());
					JumpHelper.jump(SettingActivity.this, key, b);
				}
			}
		});

	}
}
