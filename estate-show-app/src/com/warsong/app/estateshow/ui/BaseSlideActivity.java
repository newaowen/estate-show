package com.warsong.app.estateshow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.warsong.app.estateshow.R;
import com.warsong.app.estateshow.helper.ActivityRecordHelper;
import com.warsong.app.estateshow.util.GeneralUtil;

public class BaseSlideActivity extends SlidingFragmentActivity {

	protected ActivityRecordHelper recordHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		recordHelper = new ActivityRecordHelper(this);
	}

	protected void onResume() {
		super.onResume();
		recordHelper.onResume();
	}

	@Override
	public void startActivity(Intent intent) {
		recordHelper.startActivity(intent);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			//菜单按键打开/关闭左侧菜单
			getSlidingMenu().toggle();
			break;
		case KeyEvent.KEYCODE_BACK:
			recordHelper.setResult();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 注意在onResume之前执行
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		recordHelper.onActivityResult(requestCode, resultCode, data);
	}

	protected void setTitleText(int id) {
		setTitleText(getResources().getString(id));
	}
	
	protected TextView getTitleTextView() {
		return (TextView) findViewById(R.id.title_text);
	}
	
	protected View getTitleBar() {
		return findViewById(R.id.title_bar);
	}

	protected void setTitleText(String text) {
		TextView tv = (TextView) findViewById(R.id.title_text);
		if (tv != null) {
			tv.setText(text);
		}
	}

	protected void initBothSideMenu() {
		createLeftMenu();
		createRightMenu();
		bindLeftMenuAction();
		bindRightMenuAction();
	}

	protected void createLeftMenu() {
		// 侧划菜单
		setBehindContentView(R.layout.menu_frame);
		// 设置sm属性
		SlidingMenu sm = getSlidingMenu();
		// 左右菜单模式
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		// 阴影
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 阴影图片
		sm.setShadowDrawable(R.drawable.shadow);
		// 基本属性(设置宽度和设置偏移是相互冲突的)
		sm.setBehindWidth(GeneralUtil.dpToPx(this, 230));
		// sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
		// 左侧菜单替换
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuListFragment()).commit();
	}

	// 禁止触屏侧滑
	public void turnOffGestureSlide() {
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	protected void createRightMenu() {
		SlidingMenu sm = getSlidingMenu();
		// 设置右侧菜单占位布局
		sm.setSecondaryMenu(R.layout.menu_frame_two);
		sm.setSecondaryShadowDrawable(R.drawable.right_shadow);
		// 右侧菜单替换
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame_two, new ContactListFragment()).commit();
	}

	protected void bindLeftMenuAction() {
		ImageView titleLeft = (ImageView) findViewById(R.id.left_btn);
		if (titleLeft != null) {
			titleLeft.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
					getSlidingMenu().toggle();
				}
			});
		}
	}

	protected void bindRightMenuAction() {
		ImageView titleRight = (ImageView) findViewById(R.id.right_btn);
		if (titleRight != null) {
			titleRight.setOnClickListener(new View.OnClickListener() {
	
				@Override
				public void onClick(View arg0) {
					SlidingMenu sm = getSlidingMenu();
					if (!sm.isSecondaryMenuShowing()) {
						sm.showSecondaryMenu();
					} else {
						sm.showContent();
					}
				}
			});
		}
	}

}
