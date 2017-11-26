package com.cog.api.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cog.api.model.User;
import com.cog.api.security.JwtUser;
import com.cog.api.security.JwtUserAuthentication;
import com.cog.api.security.SecurityUtils;

@RestController
@RequestMapping("/api/1.0/auth")
public class AuthController{
	private MongoTemplate mongoTemplate;
	
	@Autowired
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;	
	}
	
	
	@PostMapping("/login")
	public AuthObject login() throws AuthenticationException{
		JwtUserAuthentication authentication = SecurityUtils.getAuthentication();
		JwtUser jwtUser= authentication.getPrincipal();
		return new AuthObject(authentication.getCredentials(), jwtUser.getExpiredDate(), (User)authentication.getDetails());
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
		//TODO refresh token
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
