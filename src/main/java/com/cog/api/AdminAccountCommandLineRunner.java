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
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		Query q = new Query(Criteria.where("username").is(ADMIN_USERNAME));
		User user = this.mongoTemplate.findOne(q, User.class);
		if(user == null) {
			//create admin
			log.info("creating User admin...");
			User u = new User(ADMIN_USERNAME);
			u.setPassword("studycogen");
			u.addRole(User.ROLE_ADMIN);
			u.addRole(User.ROLE_TEACHER);
			u.addRole(User.ROLE_USER);
			
			this.mongoTemplate.save(u);
			log.info("User admin created.");
		}
		
	}
}
