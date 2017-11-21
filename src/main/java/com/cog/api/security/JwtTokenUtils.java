package com.cog.api.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.www.NonceExpiredException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenUtils {
	private static Log log = LogFactory.getLog(JwtTokenUtils.class);

	private static String secret = "cognitiven-he";
	private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

	public static String generate(JwtUser jwtUser, Collection<GrantedAuthority> authorities) {
		Claims claims = Jwts.claims().setId(jwtUser.getId()).setSubject(jwtUser.getUsername())
				.setExpiration(jwtUser.getExpiredDate());
		String roles = authorities2String(authorities);
		claims.put("roles", roles);

		return Jwts.builder().setClaims(claims).signWith(signatureAlgorithm, secret).compact();
	}

	public static JwtUserAuthentication parse(String token) {
		JwtUserAuthentication authentication = null;
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
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList((String) body.get("roles"));
		JwtUser jwtUser = new JwtUser(body.getId(), username, body.getExpiration());
		authentication = new JwtUserAuthentication(jwtUser, grantedAuthorities);
		return authentication;
	}

	private static String authorities2String(Collection<GrantedAuthority> authorities) {
		ArrayList<String> sa = new ArrayList<String>(authorities.size());
		for (GrantedAuthority ga : authorities) {
			sa.add(ga.getAuthority());
		}
		return String.join(",", sa);
	}
}
