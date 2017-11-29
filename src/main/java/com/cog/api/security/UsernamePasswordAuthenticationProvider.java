package com.cog.api.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.cog.api.model.User;

@Component
public class UsernamePasswordAuthenticationProvider extends AbstractJwtAuthenticationProvider {
	private Log log = LogFactory.getLog(this.getClass());
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public Authentication authenticate() throws AuthenticationException {
		String origin = this.getParameter("origin");
		if(origin == null || !origin.equals("cogen")) {
			return null;
		}
		log.info("authenticating by username and password");
		String username = this.getParameter("username");
		String password = this.getParameter("password");
		Query q = new Query(Criteria.where("username").is(username));
		User user = this.mongoTemplate.findOne(q, User.class);
		if(user!= null && user.validatePassword(password)) {
			JwtUserAuthentication auth = createAuthenticationFromUser(user);
			log.info("authenticate successfully by username and password");
			return auth;
		} else {
			throw new BadCredentialsException("Username or password error.");
		}
	}

}
