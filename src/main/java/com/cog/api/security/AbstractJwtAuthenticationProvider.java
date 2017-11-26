package com.cog.api.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.fasterxml.jackson.databind.JsonNode;

public abstract class AbstractJwtAuthenticationProvider implements AuthenticationProvider {
	private JsonNode parameters;
	
	@Override
	public boolean supports(Class<?> authentication) {
		return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
	}
	
	@Override
	public final Authentication authenticate(Authentication authentication) throws AuthenticationException {
		this.parameters = (JsonNode)authentication.getCredentials();
		return this.authenticate();
	}
	
	protected abstract Authentication authenticate() throws AuthenticationException;
	
	protected String getParameter(String name) {
		JsonNode node = this.parameters.get(name);
		return (node != null)?node.asText():null;
	}

}
