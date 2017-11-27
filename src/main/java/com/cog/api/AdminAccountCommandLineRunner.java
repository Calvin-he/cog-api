package com.cog.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.cog.api.model.User;

@Component
public class AdminAccountCommandLineRunner implements ApplicationRunner {
	private final Log log = LogFactory.getLog(AdminAccountCommandLineRunner.class);
	
	private static final String ADMIN_USERNAME = "admin";
	
	private static final String PLAIN_USERNAME = "cogen";
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.createUserIfNotExists(ADMIN_USERNAME, "studycogen", new String[]{User.ROLE_ADMIN, User.ROLE_TEACHER, User.ROLE_USER});
		this.createUserIfNotExists(PLAIN_USERNAME, "test", new String[]{User.ROLE_USER});
	}
	
	private boolean createUserIfNotExists(String username,  String password, String[] roles) {
		Query q = new Query(Criteria.where("username").is(username));
		User user = this.mongoTemplate.findOne(q, User.class);
		if(user == null) {
			//create account
			log.info(String.format("creating User '%s'...", username));
			User u = new User(username);
			u.setPassword(password);
			for(String role:roles) {
				u.addRole(role);
			}
			this.mongoTemplate.save(u);
			log.info(String.format("User '%s' created.",username));
			return true;
		} else {
			return false;
		}
	}
}
