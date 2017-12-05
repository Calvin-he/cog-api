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

	
	@NotNull
	private String wxAppId;
	
	@NotNull
	private String wxSecret;
	
	@NotNull
	private String wxToken;

	private String wxEncodingAESKey;

	@NotNull
	private String wxMchId;
	
	@NotNull
	private String wxMchApiKey;
	
	private String wxMchCertPath;
	
	@NotNull
	private String wxNotifyUrl;
	
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

	public String getWxAppId() {
		return wxAppId;
	}

	public void setWxAppId(String wxAppId) {
		this.wxAppId = wxAppId;
	}

	public String getWxSecret() {
		return wxSecret;
	}

	public void setWxSecret(String wxSecret) {
		this.wxSecret = wxSecret;
	}

	public String getWxToken() {
		return wxToken;
	}

	public void setWxToken(String wxToken) {
		this.wxToken = wxToken;
	}

	public String getWxEncodingAESKey() {
		return wxEncodingAESKey;
	}

	public void setWxEncodingAESKey(String wxEncodingAESKey) {
		this.wxEncodingAESKey = wxEncodingAESKey;
	}

	public String getWxMchId() {
		return wxMchId;
	}

	public void setWxMchId(String wxMchId) {
		this.wxMchId = wxMchId;
	}

	public String getWxMchApiKey() {
		return wxMchApiKey;
	}

	public void setWxMchApiKey(String wxMchApiKey) {
		this.wxMchApiKey = wxMchApiKey;
	}

	public String getWxMchCertPath() {
		return wxMchCertPath;
	}

	public void setWxMchCertPath(String wxMchCertPath) {
		this.wxMchCertPath = wxMchCertPath;
	}

	public String getWxNotifyUrl() {
		return wxNotifyUrl;
	}

	public void setWxNotifyUrl(String wxNotifyUrl) {
		this.wxNotifyUrl = wxNotifyUrl;
	}
}
