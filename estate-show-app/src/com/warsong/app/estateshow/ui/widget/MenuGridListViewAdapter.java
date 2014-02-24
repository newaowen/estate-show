package com.warsong.app.estateshow.ui.widget;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.warsong.app.estateshow.R;
import com.warsong.app.estateshow.misc.MenuIconHelper;
import com.warsong.app.estateshow.misc.MenuItemClickHandler;
import com.warsong.app.estateshow.model.MenuItemModel;
import com.warsong.app.estateshow.util.GeneralUtil;

/**
 * 菜单listview apdater
 */
public class MenuGridListViewAdapter extends GridListViewBaseAdapter {

	protected List<MenuItemModel> menus;
	
	public MenuGridListViewAdapter(Context ctx, List<MenuItemModel> menusData, int column) {
		super(ctx);
		this.mNumColumns = column;
		this.menus = menusData;
		this.autoFitColumn = true;
		this.setOnItemClickListener(new ItemClickListener() {
			
			@Override
			public void onItemClicked(View v, int position, long itemId) {
				new MenuItemClickHandler(mContext, menus.get(position)).exec();
			}
		});
	}

	@Override
	public MenuItemModel getItem(int position) {
		return menus.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return menus.size();
	}
	
	@Override
	public ViewGroup getRowView() {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return (ViewGroup)inflater.inflate(R.layout.menu_grid_row, null);
	}
	
	protected int getHorizontalPadding() {
		return (int)(GeneralUtil.dpToPx(mContext, (float)40));
	}
	
	@Override
	protected View getItemView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.menu_grid_item, parent, false);
		}  
		
		final MenuItemModel menuItem = menus.get(position);
		if (menuItem != null) {
			TextView tv = (TextView)convertView.findViewById(R.id.text);
			tv.setText(menuItem.getTitle());
			
			ImageView img = (ImageView) convertView.findViewById(R.id.image);
			int resId = Integer.valueOf(menuItem.getIcon());
			img.setImageDrawable(MenuIconHelper.getGridIconDrawable(mContext, resId));
		}

		return convertView;
	}

}