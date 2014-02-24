package com.warsong.app.estateshow.manager;

import java.util.List;

import android.content.Context;

import com.warsong.app.estateshow.model.ConfigModel;
import com.warsong.app.estateshow.model.ContactModel;
import com.warsong.app.estateshow.model.GlobalModel;
import com.warsong.app.estateshow.model.LocationModel;
import com.warsong.app.estateshow.model.MenuItemModel;
import com.warsong.app.estateshow.model.NewsModel;
import com.warsong.app.estateshow.model.SpmModel;
import com.warsong.app.estateshow.util.GeneralUtil;

/**
 * app数据管理器
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-19 上午11:30:41
 */
public class AppDataManager {
	
	private Context context;

	private boolean isDebug;
	
	private GlobalModel globalModel;

	private String globalDataUrl;
	
	private String wxAppId;
	
	private String wxAppKey;
	
	private String shareClickUrl;

	private static AppDataManager instance;
	
	public static AppDataManager getInstance() {
		if (instance == null) {
			throw new RuntimeException("AppDataManager must create by createInstance(context)");
		}
		return instance;
	}
	
	public static AppDataManager createInstance(Context ctx) {
		if (instance == null) {
			synchronized (AppDataManager.class) {
				instance = new AppDataManager(ctx);
			}
		}
		return instance;
	}
	
	protected AppDataManager(Context ctx) {
		this.context = ctx;
		loadMetaData();
	}
	
	/**
	 * 从manifest文件中加载配置数据
	 */
	private void loadMetaData() {
		isDebug = GeneralUtil.isDebug(context);
		globalDataUrl = GeneralUtil.getConfigFromManifest(context, "global_data_url");
		wxAppId = GeneralUtil.getConfigFromManifest(context, "weixin_app_id");
		wxAppKey = GeneralUtil.getConfigFromManifest(context, "weixin_app_key");
		shareClickUrl = GeneralUtil.getConfigFromManifest(context, "share_click_url");
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
	
	public SpmModel getSpmModel() {
		return globalModel.getSpm();
	}
	
	public String getWxAppId() {
		return wxAppId;
	}

	public void setWxAppId(String wxAppId) {
		this.wxAppId = wxAppId;
	}

	public String getWxAppKey() {
		return wxAppKey;
	}

	public void setWxAppKey(String wxAppKey) {
		this.wxAppKey = wxAppKey;
	}

	public String getShareClickUrl() {
		return shareClickUrl;
	}

	public void setShareClickUrl(String shareClickUrl) {
		this.shareClickUrl = shareClickUrl;
	}
	
}
