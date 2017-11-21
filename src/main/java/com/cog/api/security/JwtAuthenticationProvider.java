package com.cog.api.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtUserAuthentication userAuth = ((JwtUserAuthentication)authentication);
		String credential = userAuth.getCredentials();
		JwtUserAuthentication auth= JwtTokenUtils.parse(credential);
		return auth;
	}

    @Override
    public boolean supports(Class<?> aClass) {
        return (JwtUserAuthentication.class.isAssignableFrom(aClass));
    }


}
