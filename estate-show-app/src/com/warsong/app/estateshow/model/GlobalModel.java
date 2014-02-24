package com.warsong.app.estateshow.model;

import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.warsong.app.estateshow.util.StringUtil;

/**
 * app内使用全局的数据模型
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-19 上午11:04:02
 */
public class GlobalModel {
	//app配置
	private ConfigModel config;
	//通讯录
	private ContactModel cellPhone;
	//经纬度
	private LocationModel location;
	//菜单
	private List<MenuItemModel> menu;
	//首页新闻
	private NewsModel newsTurn;
	//数据统计url
	private SpmModel spm;

	/**
	 * 从json中构造出完整对象
	 * @param json
	 */
	public static GlobalModel buildFromJson(String json) {
		GlobalModel result = null;
		try {
			// 全局json对象
			JSONObject object = new JSONObject(json);
			boolean flag = (boolean) object.getBoolean("success");
			// success为true时才开始解析
			if (flag) {
				String data = object.optString("data");
				if (!StringUtil.isEmpty(data)) {
					//使用fastjson解析数据
					result = (GlobalModel)JSON.parseObject(data, GlobalModel.class);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public ConfigModel getConfig() {
		return config;
	}

	public void setConfig(ConfigModel appConfig) {
		this.config = appConfig;
	}

	public ContactModel getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(ContactModel contact) {
		this.cellPhone = contact;
	}
//
	public LocationModel getLocation() {
		return location;
	}

	public void setLocation(LocationModel location) {
		this.location = location;
	}

	public List<MenuItemModel> getMenu() {
		return menu;
	}

	public void setMenu(List<MenuItemModel> menu) {
		this.menu = menu;
	}
 
	public NewsModel getNewsTurn() {
		return newsTurn;
	}

	public void setNewsTurn(NewsModel news) {
		this.newsTurn = news;
	}

	public SpmModel getSpm() {
		return spm;
	}

	public void setSpm(SpmModel spm) {
		this.spm = spm;
	}

}
