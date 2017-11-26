package com.cog.api.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {
	private Log log = LogFactory.getLog(this.getClass());

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("authorizate by jwt token");
		String token = (String)authentication.getCredentials();
		JwtUserAuthentication auth= JwtUserAuthentication.createFromToken(token);
		auth.setAuthenticated(true);
		log.info("authorizate successfully by jwt token");
		return auth;
	}

    @Override
    public boolean supports(Class<?> aClass) {
        return (PreAuthenticatedAuthenticationToken.class.isAssignableFrom(aClass));
    }


}