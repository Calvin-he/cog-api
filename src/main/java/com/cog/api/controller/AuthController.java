package com.cog.api.controller;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cog.api.model.User;
import com.cog.api.security.JwtTokenUtils;
import com.cog.api.security.JwtUser;
import com.cog.api.security.JwtUserAuthentication;
import com.cog.api.security.SecurityUtils;

@RestController
@RequestMapping("/auth")
public class AuthController{
	private MongoTemplate mongoTemplate;
	
	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;	
	}
	
	
	@PostMapping("/login")
	public AuthObject login(@RequestBody Map<String, Object> credentials) {
		String origin = (String) credentials.get("origin");
		if(StringUtils.isEmpty(origin)) {
			throw new RuntimeException("Invalid input");
		}
		
		AuthObject authObj = null;
		if(origin.equals("cogen")) {
			String username = (String)credentials.get("username");
			String password = (String)credentials.get("password");
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			    throw new RuntimeException("Invalid credentials");
			}
			Query q = new Query(Criteria.where("username").is(username));
			User user = this.mongoTemplate.findOne(q, User.class);
			if(user != null && user.validatePassword(password)) {
				authObj = createAuthObjectFromUser(user);
			}else {
				throw new RuntimeException("Username or password error");
			}
		}else if(origin.equals("wechat")) {
			throw new RuntimeException("Not implemented for wechat authentication now");
		}else {
			throw new RuntimeException("invalid input");
		}
		return authObj;
	}


	private AuthObject createAuthObjectFromUser(User user) {
		Date expiredDate = new Date(System.currentTimeMillis() + 24*3600);
		JwtUser jwtUser = new JwtUser(user.get_id(), user.getUsername(), expiredDate, user.getRoles().toArray(new String[0]));;
		String token = JwtTokenUtils.generate(jwtUser);
		return new AuthObject(token, expiredDate, user);
	}
	
	@GetMapping("/user")
	public User getMySelf() {
		JwtUserAuthentication authentication = SecurityUtils.getAuthentication();
		JwtUser jwtUser= authentication.getPrincipal();
		Query query = new Query(Criteria.where("_id").is(jwtUser.getId()));
		User user = this.mongoTemplate.findOne(query, User.class);
		return user;
	}
	
	@GetMapping("/refresh")
	public AuthObject refresh() {
		throw new RuntimeException("Not implemented yet");
	}
	
	@GetMapping("/logout")
	public void logout() {
		throw new RuntimeException("Not implemented yet");
	}
	
}

class AuthObject {
	String token;
	Date expired;
	User user;
	
	AuthObject(String token, Date expired, User user) {
		this.token = token;
		this.expired = expired;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public Date getExpired() {
		return expired;
	}

	public User getUser() {
		return user;
	}
	
}
