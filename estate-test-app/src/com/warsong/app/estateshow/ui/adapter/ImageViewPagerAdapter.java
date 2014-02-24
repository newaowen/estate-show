package com.warsong.app.estateshow.ui.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.warsong.app.estateshow.helper.JumpHelper;
import com.warsong.app.estateshow.misc.ImageLoader;
import com.warsong.app.estateshow.model.NewsModel;
import com.warsong.wb.estate.R;

/**
 * 首页图片swiper adpater
 */
public class ImageViewPagerAdapter extends PagerAdapter {
	private Context context;
	private NewsModel newsData;
	private ImageLoader imageLoader = new ImageLoader();

	public ImageViewPagerAdapter(Context context, NewsModel data) {
		this.context = context;
		this.newsData = data;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (newsData != null) {
			List<String> photos = newsData.getPhotos();
			if (photos != null) {
				count = photos.size();
			}
		}
		return count;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}
	
	/**
	 * 初始化时就默认执行多次？？
	 */
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		View view = LayoutInflater.from(context).inflate(R.layout.image_vp_item, null);
		final ImageView img = (ImageView) view.findViewById(R.id.image);

		List<String> photos = newsData.getPhotos();
		if (photos != null && photos.size() > position) {
			String url = photos.get(position);
			imageLoader.load(url, new ImageLoader.ImageLoaderCallback() {

				@Override
				public void onSuccess(String url, Bitmap bitmap) {
					img.setImageBitmap(bitmap);
					img.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// 跳转到news
							List<String> hrefs = newsData.getHrefs();
							List<String> titles = newsData.getTitles();
							if (hrefs != null && titles != null && hrefs.size() > position
									&& titles.size() > position) {
								String href = hrefs.get(position);
								Bundle b = new Bundle();
								b.putString("url", href);
								b.putString("title", titles.get(position));
								JumpHelper.jump(context, JumpHelper.KEY_NEWS, b);
							}
						}
					});
				}

				@Override
				public void onFail(String url) {
				}
			});
		}
		((ViewPager) container).addView(view, 0);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}
}
