package com.cog.api.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.cog.api.model.User;
import com.cog.api.wechat.WxService;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@Component
public class WechatAuthenticationProvider extends AbstractJwtAuthenticationProvider {
	private final Log logger = LogFactory.getLog(WechatAuthenticationProvider.class);

	@Autowired
	private WxService wxService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	protected Authentication authenticate() throws AuthenticationException {
		String origin = this.getParameter("origin");
		if ("wechat".equals(origin)) {
			try {
				logger.info("Start authenticating by Wechat....");
				String code = this.getParameter("code");
				WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.getWxMpService().oauth2getAccessToken(code);
				String openId = wxMpOAuth2AccessToken.getOpenId();
				Query query = new Query(Criteria.where("wx_openid").is(openId));
				User user = this.mongoTemplate.findOne(query, User.class);
				if (user == null) {
					WxMpUser wxMpUser = wxService.getWxMpService().oauth2getUserInfo(wxMpOAuth2AccessToken, null);
					user = this.convertWxMpUser2User(wxMpUser);
					this.mongoTemplate.save(user);
				}
				logger.info("Authenticated successfully by Wechat.");
				JwtUserAuthentication auth = createAuthenticationFromUser(user);
				return auth;

			} catch (WxErrorException wxe) {
				logger.error("Failed to authenticate by Wechat.");
				throw new AuthenticationServiceException("Failed to anthenticate by Wechat", wxe);
			}
		} else {
			return null;
		}
	}

	private User convertWxMpUser2User(WxMpUser wxMpUser) {
		User u = new User(wxMpUser.getOpenId());
		u.setNickname(wxMpUser.getNickname());
		u.setAvatar(wxMpUser.getHeadImgUrl());
		u.setWx_openid(wxMpUser.getOpenId());
		u.setSex(wxMpUser.getSexId());
		u.setCity(wxMpUser.getCountry() + "|" + wxMpUser.getProvince() + "|" + wxMpUser.getCity());
		u.addRole(User.ROLE_USER);
		return u;
	}

}
