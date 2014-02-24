package com.warsong.app.estateshow.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.warsong.app.estateshow.R;
import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.model.MenuItemModel;
import com.warsong.app.estateshow.model.NewsModel;
import com.warsong.app.estateshow.ui.adapter.ImageViewPagerAdapter;
import com.warsong.app.estateshow.ui.widget.ImageViewPagerIndicator;
import com.warsong.app.estateshow.ui.widget.MenuGridListViewAdapter;

/**
 * 主界面
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-18 下午8:00:47
 */
public class MainActivity extends BaseSlideActivity {

	private static final String TAG = "MainActivity";

	private ViewGroup contentView;
	private ViewPager viewPager;
	private ImageViewPagerIndicator vpIndicator;
	private ImageViewPagerAdapter viewPagerAdapter;
	private ViewGroup imageVPIndicatorBox;
	private TextView imageTitle;

	private NewsModel newsData;

	protected ListFragment leftFragment;
	protected ListFragment rightFragment;

	private Handler handler;
	private static final int MSG_NAV_VIEWPAGER = 1;
	private static final int NAV_INTERVAL = 3000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentView = (ViewGroup)LayoutInflater.from(this).inflate(R.layout.main, null);
		setContentView(contentView);
		initView();
	}

	@SuppressLint("HandlerLeak")
	private void initView() {
		setTitleText(R.string.main_title);
		initBothSideMenu();

		//view pager是listView的header
		ListView listView = (ListView) findViewById(R.id.list);
		View header = LayoutInflater.from(this).inflate(R.layout.main_viewpager, listView, false);
		listView.addHeaderView(header);
		
		List<MenuItemModel> menus = AppDataManager.getInstance().getMenus();
		List<MenuItemModel> newMenus = new ArrayList<MenuItemModel>();
		//拷贝新菜单（注意不能在原对象上修改，会影响左侧菜单的）
		if (menus != null && menus.size() > 1) {
			for (int i = 1; i < menus.size(); i++) {
				newMenus.add(menus.get(i));
			}
		}
		MenuGridListViewAdapter mAdapter = new MenuGridListViewAdapter(this, newMenus, 4);
		listView.setAdapter(mAdapter);

		//appDataManager可能未初始化的问题
		try {
			newsData = AppDataManager.getInstance().getNews();
		} catch (Exception e) {
			finish();
			return;
		}
		viewPagerAdapter = new ImageViewPagerAdapter(this, newsData);
		viewPager = (ViewPager) findViewById(R.id.image_view_pager);
		viewPager.setAdapter(viewPagerAdapter);
		// view pager离屏换存
		viewPager.setOffscreenPageLimit(5);

		imageTitle = (TextView) findViewById(R.id.image_title_text);
		// 动态增加indicator
		imageVPIndicatorBox = (ViewGroup) findViewById(R.id.image_vp_indicator_box);
		vpIndicator = new ImageViewPagerIndicator(this, imageVPIndicatorBox);
		vpIndicator.setSelectedListener(new ImageViewPagerIndicator.SelectedListener() {

			@Override
			public void run(int index) {
				List<String> titles = newsData.getTitles();
				if (titles != null && titles.size() > index) {
					imageTitle.setText(newsData.getTitles().get(index));
				}
			}
		});
		vpIndicator.setViewPager(viewPager);

		// timer消息处理
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_NAV_VIEWPAGER:
					autoNavViewPager();
					break;
				default:
					break;
				}
			}
		};
	}

	protected void onResume() {
		super.onResume();
		startTimer();
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopTimer();
	}

	private Timer mTimer;
	private CustomeTimerTask mTimerTask;

	public void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer(true);
		}

		if (mTimerTask != null) {
			mTimerTask.cancel(); // 将原任务从队列中移除
		}

		mTimerTask = new CustomeTimerTask(); // 新建一个任务
		mTimer.schedule(mTimerTask, NAV_INTERVAL, NAV_INTERVAL);
	}

	private void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}

	class CustomeTimerTask extends TimerTask {
		@Override
		public void run() {
			if (handler != null) {
				Message message = Message.obtain(handler, MSG_NAV_VIEWPAGER);
				handler.sendMessage(message);
				Log.i(TAG, "autonav on timer");
			}
		}
	}

	private void autoNavViewPager() {
		if (viewPager == null || viewPagerAdapter == null) {
			return;
		}
		int i = viewPager.getCurrentItem() + 1;
		int count = viewPagerAdapter.getCount();
		if (count > 0 && i >= count) {
			i = 0;
		}
		viewPager.setCurrentItem(i, true);
	}

}
