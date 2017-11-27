package com.cog.api.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="series")
public class Series extends AbstractDocument {
	private String title;
	private String desc;
	private String bannerId;
	private String bannerPath;
	private Long price; // fen
	private String noticeForPurchase;
	private List<String> lessonList = new ArrayList<String>(); //a list of lesson ids
	private List<String> freeLessons = new ArrayList<String>(); // a list of lesson ids
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getBannerId() {
		return bannerId;
	}
	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}
	public void setBannerPath(String bannerPath) {
		this.bannerPath = bannerPath;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	public String getNoticeForPurchase() {
		return noticeForPurchase;
	}
	public void setNoticeForPurchase(String noticeForPurchase) {
		this.noticeForPurchase = noticeForPurchase;
	}
	public List<String> getLessonList() {
		return lessonList;
	}
	public void setLessonList(List<String> lessonList) {
		this.lessonList = lessonList;
	}
	public List<String> getFreeLessons() {
		return freeLessons;
	}
	public void setFreeLessons(List<String> freeLessons) {
		this.freeLessons = freeLessons;
	}
	public String getBannerPath() {
		return bannerPath;
	}
}
