package com.warsong.app.estateshow.model;

import java.util.List;

public class NewsModel {

	private List<String> photos;
	
	private List<String> titles;
	
	private List<String> hrefs;

	public List<String> getPhotos() {
		return photos;
	}

	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}

	public List<String> getTitles() {
		return titles;
	}

	public void setTitles(List<String> titles) {
		this.titles = titles;
	}

	public List<String> getHrefs() {
		return hrefs;
	}

	public void setHrefs(List<String> hrefs) {
		this.hrefs = hrefs;
	}
	
}
