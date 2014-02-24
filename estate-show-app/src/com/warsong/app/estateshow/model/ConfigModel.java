package com.warsong.app.estateshow.model;

/**
 * 服务端下发的app配置
 * 
 * @author newaowen@gmail.com
 * @date 2013-10-19 下午12:52:10
 */
public class ConfigModel {

	private String id;
	
	private String company;
	
	private String building;
	
	private String appVersion;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String buiding) {
		this.building = buiding;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
}
