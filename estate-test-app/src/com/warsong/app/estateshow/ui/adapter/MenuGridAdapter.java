package com.warsong.app.estateshow.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.warsong.app.estateshow.misc.MenuIconHelper;
import com.warsong.app.estateshow.misc.MenuItemClickHandler;
import com.warsong.app.estateshow.model.MenuItemModel;
import com.warsong.wb.estate.R;

public class MenuGridAdapter extends BaseAdapter {
	private Context context;
	List<MenuItemModel> menus;

	public MenuGridAdapter(Context context, List<MenuItemModel> menus) {
		this.context = context;
		this.menus = menus;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.menu_grid_item, null);
		}  
		
		final MenuItemModel menuItem = menus.get(position);
		
		TextView tv = (TextView)convertView.findViewById(R.id.text);
		tv.setText(menuItem.getTitle());
		
		ImageView img = (ImageView) convertView.findViewById(R.id.image);
		int resId = Integer.valueOf(menuItem.getIcon());
		img.setImageDrawable(MenuIconHelper.getGridIconDrawable(context, resId));
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new MenuItemClickHandler(context, menuItem).exec();
			}
		});

		return convertView;
	}

	@Override
	public int getCount() {
		return menus.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
}
