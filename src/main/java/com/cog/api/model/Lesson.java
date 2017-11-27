package com.cog.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="lessons")
public class Lesson extends AbstractDocument{
	
	private String title;
	private String content;
	private String mediaId;
	private String mediaPath;
	private String mediaId2;
	private String mediaPath2;
	
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
	public String getMediaId() {
		return mediaId;
	}
	public String getMediaId2() {
		return mediaId2;
	}
	public void setMediaId2(String mediaId2) {
		this.mediaId2 = mediaId2;
	}
	public String getMediaPath2() {
		return mediaPath2;
	}
	public void setMediaPath2(String mediaPath2) {
		this.mediaPath2 = mediaPath2;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getMediaPath() {
		return mediaPath;
	}
	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}	
}
