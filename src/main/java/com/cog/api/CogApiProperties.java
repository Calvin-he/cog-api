package com.cog.api;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "cog.api")
@Validated
public class CogApiProperties {
	@NotNull
	private String mediaRoot;
	
	@NotNull
	private String mediaWebRoot;

	public String getMediaRoot() {
		return mediaRoot;
	}

	public void setMediaRoot(String mediaRoot) {
		this.mediaRoot = mediaRoot;
	}

	public String getMediaWebRoot() {
		return mediaWebRoot;
	}

	public void setMediaWebRoot(String mediaWebRoot) {
		this.mediaWebRoot = mediaWebRoot;
	}
	
}
