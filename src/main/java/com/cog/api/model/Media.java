package com.cog.api.model;

import org.springframework.data.mongodb.core.index.Indexed;

public class Media extends AbstractDocument{
	@Indexed(unique = true)
	private String path;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
