package com.warsong.app.estateshow.ui;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.warsong.app.estateshow.R;
import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.misc.MenuIconHelper;
import com.warsong.app.estateshow.misc.MenuItemClickHandler;
import com.warsong.app.estateshow.model.MenuItemModel;

/**
 * test用的列表fragment
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-19 上午11:08:15
 */
public class MenuListFragment extends ListFragment {

	private CustomAdapter adapter;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.menu_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		try {
			List<MenuItemModel> menus = AppDataManager.getInstance().getMenus(); 
			adapter = new CustomAdapter(getActivity(), menus);
			setListAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class CustomAdapter extends BaseAdapter {
		private Context context;
		List<MenuItemModel> menus;
		
		public CustomAdapter(Context context, List<MenuItemModel> menus) {
			this.context = context;
			this.menus = menus;
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

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.menu_row, null);
			}
			
			final MenuItemModel menuItem = menus.get(position);
			//设置文字标题
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(menuItem.getTitle());
			//获取图片资源
			ImageView img = (ImageView) convertView.findViewById(R.id.row_icon);
			int id = Integer.valueOf(menuItem.getIcon());
			img.setImageDrawable(MenuIconHelper.getMenuIconDrawable(context, id));
			
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new MenuItemClickHandler(context, menuItem).exec();
				}
			});
			
			return convertView;
		}

	}
}
