package com.warsong.app.estateshow.manager;

import java.util.List;

import com.warsong.app.estateshow.model.ConfigModel;
import com.warsong.app.estateshow.model.ContactModel;
import com.warsong.app.estateshow.model.GlobalModel;
import com.warsong.app.estateshow.model.LocationModel;
import com.warsong.app.estateshow.model.MenuItemModel;
import com.warsong.app.estateshow.model.NewsModel;

/**
 * app数据管理器
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-19 上午11:30:41
 */
public class AppDataManager {

	private boolean isDebug;
	
	private GlobalModel globalModel;

	private String globalDataUrl = "http://www.yangdx.com/index.php/app/get_building_info/%7B699F141C-3C80-AC4F-FA4A-7764A125F2CB%7D/";

	private static AppDataManager instance;
	
	public static AppDataManager getInstance() {
		if (instance == null) {
			instance = new AppDataManager();
		}
		
		return instance;
	}
	
	protected AppDataManager() {
		isDebug = true;
	}
	
	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public String getGlobalDataUrl() {
		return globalDataUrl;
	}

	public void setGlobalDataUrl(String globalDataUrl) {
		this.globalDataUrl = globalDataUrl;
	}

	public GlobalModel getGlobalModel() {
		return globalModel;
	}

	public void setGlobalModel(GlobalModel globalModel) {
		this.globalModel = globalModel;
	}
	
	public ConfigModel getConfig() {
		return globalModel.getConfig();
	}
	
	public List<MenuItemModel> getMenus() {
		return globalModel.getMenu();
	}
	
	public MenuItemModel getMenuByKey(String itemKey) {
		List<MenuItemModel> menus = getMenus();
		if (menus != null) {
			for (int i = 0; i < menus.size(); i++) {
				if(itemKey.equals(menus.get(i).getKey())) {
					return menus.get(i);
				}
			}
		}
		return null;
	}
	
	public ContactModel getContact() {
		return globalModel.getCellPhone();
	}
	
	public NewsModel getNews() {
		return globalModel.getNewsTurn();
	}
	
	public LocationModel getLocation() {
		return globalModel.getLocation();
	}
	
}
