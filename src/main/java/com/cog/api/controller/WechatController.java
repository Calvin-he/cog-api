package com.cog.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cog.api.model.LearningProgress;
import com.cog.api.model.Order;
import com.cog.api.model.User;
import com.cog.api.wechat.WxService;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;

import io.jsonwebtoken.lang.Assert;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.exception.WxErrorException;

@RestController
@RequestMapping("/api/1.0/wechat")
public class WechatController {
	private final Log logger = LogFactory.getLog(WechatController.class);
	
	@Autowired
	private WxService wxService;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
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

	@GetMapping("/paynotify")
	public String handlePayNotification(HttpServletRequest request, HttpServletResponse response) {
		try {
		    String xmlResult = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
		    WxPayOrderNotifyResult result = this.wxService.getWxPayService().parseOrderNotifyResult(xmlResult);
		    Order order = this.mongoTemplate.findById(new ObjectId(result.getOutTradeNo()), Order.class);
		    if(order == null) {
			    order = new Order();
			    order.setOutTradeNo(result.getOutTradeNo());
			    order.setTransactionId(result.getTransactionId());
			    order.setFee(result.getTotalFee()/100.0);
			    String seriesId = result.getAttach();
			    order.setSeriesId(seriesId);
			    
			    User user = this.mongoTemplate.findOne(Query.query(Criteria.where("wx_openid").is(result.getOpenid())), User.class);
			    order.setUserId(user.get_id());;
			    
			    SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmss");
			    Date datetime = format.parse(result.getTimeEnd());
			    order.setEndTime(datetime);
			    this.mongoTemplate.save(order);
			    
			    LearningProgress lp = new LearningProgress();
			    lp.setSeriesId(seriesId);
			    lp.setUserId(user.get_id());
			    lp.setCurProgress(0);
			    this.mongoTemplate.save(lp);
		    }
		    return WxPayNotifyResponse.success("Deal done!");
		  } catch (Exception e) {
		    logger.error("Wechat Pay callback unsuccessufll", e);
		    return WxPayNotifyResponse.fail(e.getMessage());
		  }
	}
	
	
}
