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
    	List<GrantedAuthority> authorities= AuthorityUtils.createAuthorityList(user.getRoles());
    	return new JwtUserAuthentication(user, authorities);
    }

    private JwtUserAuthentication(JwtUser user, List<GrantedAuthority> grantedAuthorities) {
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
