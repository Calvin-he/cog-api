package com.cog.api.security;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.www.NonceExpiredException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenUtils {
	private static Log log = LogFactory.getLog(JwtTokenUtils.class);

	private static String secret = "cognitiven-he";
	private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

	public static String generate(JwtUser jwtUser) {
		Claims claims = Jwts.claims().setId(jwtUser.getId()).setSubject(jwtUser.getUsername())
				.setExpiration(jwtUser.getExpiredDate());
		String roles = roles2String(jwtUser.getRoles());
		claims.put("roles", roles);

		return Jwts.builder().setClaims(claims).signWith(signatureAlgorithm, secret).compact();
	}

	public static JwtUser parse(String token) {
		Claims body = null;
		try {
			body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch(Exception e) {
			log.warn("Invalid token try to login our system");
			throw new BadCredentialsException("Invalid token");
		}
		Date expired = body.getExpiration();
		if (expired.before(new Date())) {
			throw new NonceExpiredException("Token expired");
		}

		String username = body.getSubject();
		String roles = (String)body.get("roles");
		JwtUser jwtUser = new JwtUser(body.getId(), username, body.getExpiration(), string2Roles(roles));
		return jwtUser;
	}

	private static String roles2String(String[] roles) {
		return String.join(",", roles);
	}
	
	private static String[] string2Roles(String roles) {
		return roles.split(",");
	}
}
