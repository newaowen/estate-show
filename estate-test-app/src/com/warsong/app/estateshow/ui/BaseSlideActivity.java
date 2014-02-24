package com.warsong.app.estateshow.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.warsong.app.estateshow.util.GeneralUtil;
import com.warsong.wb.estate.R;

public class BaseSlideActivity extends SlidingFragmentActivity {

	protected void setTitleText(int id) {
		setTitleText(getResources().getString(id));
	}
	
	protected void setTitleText(String text) {
		TextView tv = (TextView)findViewById(R.id.title_text);
		tv.setText(text);
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
		// 恢复操作
		// if (savedInstanceState == null) {
		// FragmentTransaction t =
		// this.getSupportFragmentManager().beginTransaction();
		// mFrag = new CustomListFragment();
		// t.replace(R.id.menu_frame, mFrag);
		// t.commit();
		// } else {
		// mFrag =
		// (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		// }

		// 设置sm属性
		SlidingMenu sm = getSlidingMenu();
		// 左右菜单模式
		sm.setMode(SlidingMenu.LEFT_RIGHT);
		// 阴影
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 阴影图片
		sm.setShadowDrawable(R.drawable.shadow);
		// above和behind的概念，没有详细解释
		// sm.setAboveOffset(R.dimen.slidingmenu_offset);
		// 基本属性(设置宽度和设置偏移是相互冲突的)
		sm.setBehindWidth(GeneralUtil.dpToPx(this, 200));
		// sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		// 禁止触屏侧滑
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// 左侧菜单替换
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new MenuListFragment()).commit();
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
		titleLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getSlidingMenu().toggle();
			}
		});
	}

	protected void bindRightMenuAction() {
		ImageView titleRight = (ImageView) findViewById(R.id.right_btn);
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
