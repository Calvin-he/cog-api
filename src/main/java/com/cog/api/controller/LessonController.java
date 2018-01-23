package com.cog.api.controller;

import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cog.api.model.Comment;
import com.cog.api.model.Lesson;
import com.cog.api.model.User;
import com.cog.api.security.JwtUser;
import com.cog.api.security.SecurityUtils;

@RestController
@RequestMapping("/api/1.0/lessons")
public class LessonController extends BaseController<Lesson> {
	
	@GetMapping("/{id}/comments")
	public List<Comment> listComment(@PathVariable String id) {
		Query q = new Query(Criteria.where("lessonId").is(id));
		return this.mongoTemplate.find(q, Comment.class);
	}
	
	@PostMapping("/{id}/comments")
	public Comment createComment(@PathVariable String id, @RequestBody Map<String, Object> body) {
		String content = (String)body.get("content");
		Comment c = new Comment();
		JwtUser juser = SecurityUtils.getCurrentUser();
		User user = this.mongoTemplate.findById(new ObjectId(juser.getId()), User.class);
		
		c.setLessonId(id);
		c.setContent(content);
		c.setUserId(user.get_id());
		c.setUserNickname(user.getNickname());
		c.setUserAvatar(user.getAvatar());
		this.mongoTemplate.save(c);
		return c;
	}
	
	@DeleteMapping("{id}/comments/{commentId}")
	public void deleteComment(@PathVariable String id, @PathVariable String commentId) {
		JwtUser juser = SecurityUtils.getCurrentUser();
		Query query;
		if(juser.isAdmin()) {
			query = new Query(Criteria.where("_id").is(commentId));
			this.mongoTemplate.remove(query, Comment.class);
		} else {
			query = new Query(Criteria.where("_id").is(commentId).andOperator(Criteria.where("username").is(juser.getUsername())));
			this.mongoTemplate.remove(query, Comment.class);
		}
	}
	
}
