package com.cog.api.security;

import java.util.Date;

public class JwtUser {
	private String id;
	private String username;
    private Date expiredDate;
    
    public JwtUser(String id, String username, Date expiredDate) {
    	this.id = id;
    	this.username = username;
    	this.expiredDate = expiredDate;
    }
    
	public String getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}
}
