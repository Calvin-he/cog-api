package com.cog.api.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="comments")
public class Comment extends AbstractDocument{
	private String lessonId;
	private String content;
	private int voteUpNum = 0;
	private int top = 0;  // higer top value, higher order
	private String username;
	private String userNickname;
	private String userAvatar;
	private String repliedTo;
	
	public String getLessonId() {
		return lessonId;
	}
	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getVoteUpNum() {
		return voteUpNum;
	}
	public void setVoteUpNum(int voteUpNum) {
		this.voteUpNum = voteUpNum;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserNickname() {
		return userNickname;
	}
	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	public String getRepliedTo() {
		return repliedTo;
	}
	public void setReplied(String repliedTo) {
		this.repliedTo = repliedTo;
	}

}
