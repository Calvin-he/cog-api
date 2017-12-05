package com.cog.api.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cog.api.CogApiProperties;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

@Component
public class WxService {
	private final WxMpService wxMpService;
	private final WxPayService wxPayService;

	@Autowired
	public WxService(CogApiProperties cogApiProperties) {
		WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
		config.setAppId(cogApiProperties.getWxAppId()); // 设置微信公众号的appid
		config.setSecret(cogApiProperties.getWxSecret()); // 设置微信公众号的app corpSecret
		config.setToken(cogApiProperties.getWxToken()); // 设置微信公众号的token
		config.setAesKey(cogApiProperties.getWxEncodingAESKey()); // 设置微信公众号的EncodingAESKey

		this.wxMpService = new WxMpServiceImpl();
		this.wxMpService.setWxMpConfigStorage(config);
		
		 WxPayConfig payConfig = new WxPayConfig();
		 payConfig.setAppId(cogApiProperties.getWxAppId());
		 payConfig.setMchId(cogApiProperties.getWxMchId());
		 payConfig.setMchKey(cogApiProperties.getWxMchApiKey());
		 payConfig.setKeyPath(cogApiProperties.getWxMchCertPath());
		 this.wxPayService = new WxPayServiceImpl();
		 this.wxPayService.setConfig(payConfig);
		
	}
	
	public WxMpService getWxMpService() {
		return this.wxMpService;
	}
	
	public WxPayService getWxPayService() {
		return this.wxPayService;
	}
}
