package com.cog.api.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cog.api.wechat.WxService;

import io.jsonwebtoken.lang.Assert;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;

@RestController
@RequestMapping("/api/1.0/wechat")
public class WechatController {

	@Autowired
	private WxService wxService;
	
	@PostMapping("/config")
	public Map<String, Object> config(@RequestBody Map<String, Object> params) throws WxErrorException {
		String url = (String)params.get("url");
		Assert.notNull(url);;
		WxJsapiSignature signature = this.wxService.getWxMpService().createJsapiSignature(url);
		Map<String, Object> result = new HashMap<String,Object>();
		result.put("debug", params.get("debug"));
		result.put("jsApiList", params.get("jsApiList"));
		result.put("appId", signature.getAppId());
		result.put("timestamp", signature.getTimestamp());
		result.put("nonceStr", signature.getNonceStr());
		result.put("signature", signature.getSignature());
		return result;
	}
	
	@GetMapping("/authorize_url")
	public Map<String, String> getAuthorizeUrl(@RequestParam("redirect_url") String redirectUrl, @RequestParam("state") String state) {
		String url = this.wxService.getWxMpService().oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAUTH2_SCOPE_USER_INFO, state);
		Map<String, String> result = new HashMap<String,String>();
		result.put("authorize_url", url);
		return result;
	}
}
