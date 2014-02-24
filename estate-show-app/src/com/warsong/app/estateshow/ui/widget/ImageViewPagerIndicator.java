package com.warsong.app.estateshow.ui.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.warsong.app.estateshow.R;

public class ImageViewPagerIndicator extends ViewPagerIndicator {

	private SelectedListener listener;
	
	public ImageViewPagerIndicator(Context context, ViewGroup container) {
		super(context, container);
	}
	
	public void setSelectedListener(SelectedListener l) {
		this.listener = l;
	}

	@Override
	public View getItemView(View convertView, int index) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.image_vp_indicator_item,
					container, false);
		}
		return convertView;
	}

	@Override
	protected void onIndicatorChanged(View v, int index, boolean active) {
		super.onIndicatorChanged(v, index, active);
		int resId = 0;
		if (active) {
			resId = R.drawable.indicator_active;
		} else {
			resId = R.drawable.indicator_normal;
		}
		v.setBackgroundResource(resId);
	}
	
	@Override
	protected void onIndicatorSelected(int index) {
		super.onIndicatorSelected(index);
		if (listener != null) {
			listener.run(index);
		}
	}
	
	public interface SelectedListener {
		public void run(int index);
	}
}
