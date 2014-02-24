package com.warsong.app.estateshow.model;

import java.util.List;

/**
 * 需兼容各种不同的菜单
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-29 下午11:49:44
 */
public class MenuItemModel {

	private String key;
	
	private String icon;
	
	private String title;
	
	private String url;
	
	private List<String> photo_urls;
	
	private List<String> photo_titles;
	
	private List<SettingItemModel> cells;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getPhoto_urls() {
		return photo_urls;
	}

	public void setPhoto_urls(List<String> photo_urls) {
		this.photo_urls = photo_urls;
	}

	public List<SettingItemModel> getCells() {
		return cells;
	}

	public void setCells(List<SettingItemModel> cells) {
		this.cells = cells;
	} 
	
	public SettingItemModel getSettingItemByKey(String key) {
		if (cells != null) {
			for (int i = 0; i < cells.size(); i++) {
				if(key.equals(cells.get(i).getKey())) {
					return cells.get(i);
				}
			}
		}
		return null;
	}

	public List<String> getPhoto_titles() {
		return photo_titles;
	}

	public void setPhoto_titles(List<String> photo_titles) {
		this.photo_titles = photo_titles;
	}
	
}
