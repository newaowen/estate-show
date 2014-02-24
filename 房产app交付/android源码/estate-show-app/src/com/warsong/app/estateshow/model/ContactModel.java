package com.warsong.app.estateshow.model;

import java.util.List;

public class ContactModel {

	private String title;
	
	private String icon;
	
	//list:name -> number
	private List<ContactKVModel> kv;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<ContactKVModel> getKv() {
		return kv;
	}

	public void setKv(List<ContactKVModel> kv) {
		this.kv = kv;
	}
	
}
