package com.cog.api.model;

import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="series")
public class Series extends AbstractDocument {
	private String title;
	private String desc;
	private Double price; // unit: yuan
	
	@Transient
	private List<Lesson> lessonList; //a list of lesson ids
	@Transient
	private LearningProgress learningProgress;
	
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public List<Lesson> getLessonList() {
		return lessonList;
	}
	public void setLessonList(List<Lesson> lessonList) {
		this.lessonList = lessonList;
	}
	public LearningProgress getLearningProgress() {
		return learningProgress;
	}
	public void setLearningProgress(LearningProgress learningProgress) {
		this.learningProgress = learningProgress;
	}
	
}
