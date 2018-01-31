package com.cog.api.security;

import java.util.Date;

public class JwtUser {
	private final String id;
	private final String username;
    private final Date expiredDate;
    private final String[] roles;
    
    public JwtUser(String id, String username, Date expiredDate, String[] roles) {
    	this.id = id;
    	this.username = username;
    	this.expiredDate = expiredDate;
    	this.roles = roles;
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
	
	public String[] getRoles() {
		return this.roles;
	}
	
	public boolean isAdmin() {
		for(String role: this.roles) {
			if(role.equals("ROLE_ADMIN")) {
				return true;
			}
		}
		return false;
	}
}
