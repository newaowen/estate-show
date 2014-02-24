package com.warsong.app.estateshow.misc;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;

import com.warsong.app.estateshow.helper.JumpHelper;
import com.warsong.app.estateshow.manager.AppDataManager;
import com.warsong.app.estateshow.model.LocationModel;
import com.warsong.app.estateshow.model.MenuItemModel;
import com.warsong.app.estateshow.util.StringUtil;

/**
 * 菜单点击处理
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-19 下午3:14:32
 */
public class MenuItemClickHandler {

	protected Context context;
	
	protected MenuItemModel menuItem;
	
	public MenuItemClickHandler(Context ctx, MenuItemModel model) {
		this.context = ctx;
		this.menuItem = model;
	}
	
	public void exec() {
		if (menuItem != null) {
			Bundle b = new Bundle();
			b.putString("title", menuItem.getTitle());
			String key = menuItem.getKey();
			if (MenuKeyEnum.KEY_ONE.equals(key)) { //到首页
				JumpHelper.jump(context, key, b);
			} else if (MenuKeyEnum.KEY_NEWS.equals(key)) { //到新闻
				String url = menuItem.getUrl();
				if (!StringUtil.isEmpty(url)) {
					b.putString("url", url);
					JumpHelper.jump(context, key, b);
				}
			} else if (MenuKeyEnum.KEY_MAP.equals(key)) {//到地图
				b.putString("title", menuItem.getTitle());
				LocationModel data = AppDataManager.getInstance().getLocation();
				if (data != null) {
					b.putString("latitude", data.getLatitude());
					b.putString("longitude", data.getLongitude());
					b.putString("titleShow", data.getTitleShow());
					b.putString("subTitleShow", data.getSubTitleShow());
				}
				JumpHelper.jump(context, key, b);
			} else if (MenuKeyEnum.KEY_PHOTOS.equals(key)) { //到图片浏览
				b.putString("title", menuItem.getTitle());
				ArrayList<String> urls = (ArrayList<String>)menuItem.getPhoto_urls();
				if (urls != null) {
					b.putStringArrayList("urls", urls);
				}
				ArrayList<String> photoTitles = (ArrayList<String>)menuItem.getPhoto_titles();
				if (urls != null) {
					b.putStringArrayList("photoTitles", photoTitles);
				}
				JumpHelper.jump(context, key, b);
			} else if (MenuKeyEnum.KEY_HTMLVIEW.equals(key)) { //到html view
				String url = menuItem.getUrl();
				if (!StringUtil.isEmpty(url)) {
					b.putString("url", url);
					JumpHelper.jump(context, key, b);
				}
			} else if (MenuKeyEnum.KEY_SETTING.equals(key)) {
				JumpHelper.jump(context, key, null);
			}
		}
	}
	
}
