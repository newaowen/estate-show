package com.warsong.app.estateshow.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.warsong.app.estateshow.info.AppInfo;
import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.misc.Version;
import com.warsong.app.estateshow.model.ConfigModel;
import com.warsong.app.estateshow.model.MenuItemModel;
import com.warsong.app.estateshow.model.SettingItemModel;
import com.warsong.wb.estate.R;

/**
 * 查看升级信息界面 当前缺少版本数据
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-29 下午10:03:37
 */
public class AppUpdateActivity extends BaseSlideActivity {

	private Button updateBtn;
	private TextView latestVersionTV;
	private TextView currentVersionTV;

	private MenuItemModel menuItem;
	private SettingItemModel settingItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.app_update);
		initView();
	}

	private void initView() {
		initBothSideMenu();

		menuItem = AppDataManager.getInstance().getMenuByKey("Setting");
		if (menuItem == null) {
			return;
		}

		settingItem = menuItem.getSettingItemByKey("update");
		if (settingItem == null) {
			return;
		}

		ConfigModel config = AppDataManager.getInstance().getConfig();
		if (config == null) {
			return;
		}

		setTitleText(menuItem.getTitle());
		String latestVersion = config.getAppVersion();
		String currentVersion = AppInfo.createInstance(this).getmProductVersion();

		latestVersionTV = (TextView) findViewById(R.id.latest_version_text);
		currentVersionTV = (TextView) findViewById(R.id.current_version_text);
		latestVersionTV.setText(String.format(
				getResources().getString(R.string.latest_version_label), latestVersion));
		currentVersionTV.setText(String.format(
				getResources().getString(R.string.current_version_label), currentVersion));

		updateBtn = (Button) findViewById(R.id.update_btn);
		// 版本比较
		if (Version.compare(currentVersion, latestVersion) < 0) {
			updateBtn.setVisibility(View.VISIBLE);
			updateBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 打开浏览器访问升级页面
					Intent i = new Intent("android.intent.action.VIEW", Uri.parse(settingItem
							.getUrl()));
					startActivity(i);
				}
			});
		} else {
			updateBtn.setVisibility(View.GONE);
		}
	}
}
