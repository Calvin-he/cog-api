package com.cog.api.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

	public static JwtUserAuthentication getAuthentication() {
		JwtUserAuthentication authentication = (JwtUserAuthentication)SecurityContextHolder.getContext().getAuthentication();
		return authentication;
	}
	
	public static JwtUser getCurrentUser() {
		return getAuthentication().getPrincipal();
	}
}
