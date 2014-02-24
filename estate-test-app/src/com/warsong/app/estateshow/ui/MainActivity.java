package com.warsong.app.estateshow.ui;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.warsong.app.estateshow.info.AppInfo;
import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.model.NewsModel;
import com.warsong.app.estateshow.ui.adapter.ImageViewPagerAdapter;
import com.warsong.app.estateshow.ui.adapter.MenuGridAdapter;
import com.warsong.app.estateshow.ui.widget.ImageViewPagerIndicator;
import com.warsong.wb.estate.R;

/**
 * 主界面
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-18 下午8:00:47
 */
public class MainActivity extends BaseSlideActivity {

	private ViewPager viewPager;
	private ImageViewPagerIndicator vpIndicator;
	private ImageViewPagerAdapter viewPagerAdapter;
	private ViewGroup imageVPIndicatorBox;
	private TextView imageTitle;

	private NewsModel newsData;
	private GridView menuGrid;
	private MenuGridAdapter menuGridAdapter;

	protected ListFragment leftFragment;
	protected ListFragment rightFragment;

	private Handler handler;
	private static final int MSG_NAV_VIEWPAGER = 1;
	private static final int NAV_INTERVAL = 3000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		initJPush();
		initView();
	}

	private void initView() {
		setTitleText(R.string.main_title);
		initBothSideMenu();

		newsData = AppDataManager.getInstance().getNews();
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
					//imageTitle.setText(String.valueOf(index));
				}
			}
		});
		vpIndicator.setViewPager(viewPager);

		// 初始化menu gridview
		menuGridAdapter = new MenuGridAdapter(this, AppDataManager.getInstance().getMenus());
		menuGrid = (GridView) findViewById(R.id.menu_grid);
		menuGrid.setAdapter(menuGridAdapter);

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

	private void initJPush() {
		boolean debug = AppInfo.createInstance(this).isDebuggable();
		JPushInterface.setDebugMode(debug);
		JPushInterface.init(this);
	}

	protected void onResume() {
		super.onResume();
		startTimer();
	}

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
