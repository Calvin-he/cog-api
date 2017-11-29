package com.cog.api.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cog.api.CogApiProperties;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

@Component
public class WxService {
	private final WxMpService wxMpService;

	@Autowired
	public WxService(CogApiProperties cogApiProperties) {
		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		config.setAppId(cogApiProperties.getWxAppId()); // 设置微信公众号的appid
		config.setSecret(cogApiProperties.getWxSecret()); // 设置微信公众号的app corpSecret
		config.setToken(cogApiProperties.getWxToken()); // 设置微信公众号的token
		config.setAesKey(cogApiProperties.getWxEncodingAESKey()); // 设置微信公众号的EncodingAESKey

		this.wxMpService = new WxMpServiceImpl();
		this.wxMpService.setWxMpConfigStorage(config);
	}
	
	public WxMpService getWxMpService() {
		return this.wxMpService;
	}
}
