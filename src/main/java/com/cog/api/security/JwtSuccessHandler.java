package com.cog.api.security;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class JwtSuccessHandler implements AuthenticationSuccessHandler{	
	private final Log log = LogFactory.getLog(JwtSuccessHandler.class);
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
    	JwtUserAuthentication auth = (JwtUserAuthentication)authentication;
    	String username = auth.getPrincipal().getUsername();
    	log.info("User '" + username + "' passed authentication");
    }
}
