package com.cog.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="lessons")
public class Lesson extends AbstractDocument{
	
	private String seriesId;
	private String title;
	private String content;
	private boolean isDraft;
	private boolean isTrial;
	private Integer sequence; // use for ordering
	
	public String getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isDraft() {
		return isDraft;
	}
	public void setDraft(boolean isDraft) {
		this.isDraft = isDraft;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public void setTrial(boolean isTrial) {
		this.isTrial = isTrial;
	}
	public boolean isTrial() {
		return isTrial;
	}
	
}
