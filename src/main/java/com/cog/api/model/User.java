package com.cog.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.cog.api.Utils;

public class User extends AbstractDocument {
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_TEACHER = "ROLE_TEACHER";
	public static final String ROLE_USER = "ROLE_USER";
	
	@Indexed(unique=true)
	private String username;
	private String avatar;
	private String city;
	private String phoneNo;
	private String email;
	private Integer sex;
	private String wx_openid;
	private String nickname;
	
	private String salt;
	private String hashed_password;
	private Set<String> roles = new TreeSet<String>();
	private List<PaidSeries> paidSeries = new ArrayList<PaidSeries>();
	
	public User(String username) {
		Assert.isTrue(!StringUtils.isEmpty(username), "Username must not be empty!");
		this.username = username;
		this.salt = String.valueOf(Math.round(Math.random() * (new Date().getTime())));
	}
	
	public String getUsername() {
		return username;
	}

	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getWx_openid() {
		return wx_openid;
	}
	public void setWx_openid(String wx_openid) {
		this.wx_openid = wx_openid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Boolean isAdmin() {
		return this.roles.contains(ROLE_ADMIN);
	}
	
	public String[] getRoles() {
		return this.roles.toArray(new String[0]);
	}
	
	public void addRole(String role) {
		this.roles.add(role);
	}
	public List<PaidSeries> getPaidSeries() {
		return paidSeries;
	}
	public void addPaidSeries(PaidSeries paidSeries) {
		this.paidSeries.add(paidSeries);
	}

	public void setPassword(String password) {
		this.hashed_password = Utils.sha1(password, this.salt);
	}
	
	public boolean validatePassword(String password) {
		String hash = Utils.sha1(password,this.salt);
		return this.hashed_password.equals(hash);
	}
}


class PaidSeries {
	String seriesId;
	Date paidDate;
	String lessonIdOfLastVisted;
	Date dateOfLastVisited;
}