package com.cog.api.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="series")
public class Series extends AbstractDocument {
	private String title;
	private String desc;
	private String bannerId;
	private String bannerPath;
	private Double price; // yuan
	private String noticeForPurchase;
	private List<String> lessonList = new ArrayList<String>(); //a list of lesson ids
	private List<String> freeLessons = new ArrayList<String>(); // a list of lesson ids
	@Transient
	private LearningProgress learningProgress;
	@Transient
	private long purchaseCount;
	
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
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
	public LearningProgress getLearningProgress() {
		return learningProgress;
	}
	public void setLearningProgress(LearningProgress learningProgress) {
		this.learningProgress = learningProgress;
	}
	public long getPurchaseCount() {
		return purchaseCount;
	}
	public void setPurchaseCount(long purchaseCount) {
		this.purchaseCount = purchaseCount;
	}
	
}
