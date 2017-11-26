package com.cog.api.security;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

public class JwtUserAuthentication extends AbstractAuthenticationToken {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3961127049575598280L;
	private JwtUser principal;
    private String credentials;

    
    public static JwtUserAuthentication createFromToken(String token) {
    	JwtUser user = JwtTokenUtils.parse(token);
    	return new JwtUserAuthentication(user, token);
    }

    protected JwtUserAuthentication(JwtUser user, String token) {
    	super(AuthorityUtils.createAuthorityList(user.getRoles()));
    	this.principal = user;
    	this.credentials = token;
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
