package com.cog.api.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="learning_progresses")
public class LearningProgress extends AbstractDocument {
	private String seriesId;
	private String userId;
	private Integer curProgress = 0;
	private Date dateOfLastVisitLesson;
	public String getSeriesId() {
		return seriesId;
	}
	public String getUserId() {
		return userId;
	}
	public Integer getCurProgress() {
		return curProgress;
	}
	public Date getDateOfLastVisitLesson() {
		return dateOfLastVisitLesson;
	}
	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setCurProgress(Integer curProgress) {
		this.curProgress = curProgress;
	}
	public void setDateOfLastVisitLesson(Date dateOfLastVisitLesson) {
		this.dateOfLastVisitLesson = dateOfLastVisitLesson;
	}
	
}
