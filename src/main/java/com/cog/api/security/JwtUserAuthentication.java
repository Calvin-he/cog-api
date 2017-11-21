package com.cog.api.security;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtUserAuthentication extends AbstractAuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3961127049575598280L;
	private JwtUser principal;
    private String credentials;

    
    public JwtUserAuthentication(String token) {
    	super(null);
    	this.credentials = token;
    }

    public JwtUserAuthentication(JwtUser user, List<GrantedAuthority> grantedAuthorities) {
    	super(grantedAuthorities);
    	this.principal = user;
    }
    


	@Override
	public String getCredentials() {
		return this.credentials;
	}

	@Override
	public JwtUser getPrincipal() {
		return this.principal;
	}
}
