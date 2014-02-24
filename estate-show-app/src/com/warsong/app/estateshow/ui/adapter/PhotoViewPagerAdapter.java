package com.warsong.app.estateshow.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.warsong.app.estateshow.R;
import com.warsong.app.estateshow.misc.ImageLoader;
import com.warsong.app.estateshow.model.PhotoModel;

/**
 * photo界面中的图片swiper adpater
 */
public class PhotoViewPagerAdapter extends PagerAdapter {
	private Context context;
	private List<PhotoModel> data;
	private ImageLoader imageLoader = new ImageLoader();

	public PhotoViewPagerAdapter(Context context, List<PhotoModel> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (data != null) {
			count = data.size();
		}
		return count;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

	/**
	 * 初始化时就开始执行多次
	 */
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = LayoutInflater.from(context).inflate(R.layout.photo_item, null);
		if (data != null && data.size() > position) {
			String url = data.get(position).url;
			
			final ProgressBar loading = (ProgressBar) view.findViewById(R.id.loading);
			loading.setVisibility(View.VISIBLE);
			//图片加载
			final ImageView img = (ImageView) view.findViewById(R.id.image);
			img.setVisibility(View.GONE);
			
			imageLoader.load(url, new ImageLoader.ImageLoaderCallback() {

				@Override
				public void onSuccess(String url, Bitmap bitmap) {
					img.setImageBitmap(bitmap);
					loading.setVisibility(View.GONE);
					img.setVisibility(View.VISIBLE);
				}

				@Override
				public void onFail(String url) {
					
				}
			});
			
			//分享控制
		}
		((ViewPager) container).addView(view, 0);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
}
