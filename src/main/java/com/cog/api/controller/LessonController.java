package com.cog.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
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
	
	@Override
	protected Lesson innerCreate(Lesson les) {
		String mediaPath = this.findMediaPathById(les.getMediaId());
		les.setMediaPath(mediaPath);
		
		String mediaPath2 = this.findMediaPathById(les.getMediaId2());
		if(!StringUtils.isEmpty(mediaPath2)) {
			les.setMediaPath2(mediaPath2);
		}
		return les;
	}
	
	@Override 
	protected Map<String, Object> innerUpdate(String id, Map<String, Object> fields) {
		Map<String, Object> m  = new HashMap<String, Object>(fields);
		String mediaId = (String)fields.get("mediaId");
		if(!StringUtils.isEmpty(mediaId)) {
			m.put("mediaPath", this.findMediaPathById(mediaId));
		}
		String mediaId2 = (String)fields.get("mediaId2");
		if(!StringUtils.isEmpty(mediaId2)) {
			m.put("mediaPath2", this.findMediaPathById(mediaId2));
		}
		return m;
	}

	@GetMapping("/{id}/comments")
	public List<Comment> listComment(@PathVariable String lid) {
		Query q = new Query(Criteria.where("lessonId").is(lid));
		return this.mongoTemplate.find(q, Comment.class);
	}
	
	@PostMapping("/{id}/comments")
	public Comment createComment(@PathVariable String lid, @RequestBody Map<String, Object> body) {
		String content = (String)body.get("content");
		Comment c = new Comment();
		JwtUser juser = SecurityUtils.getCurrentUser();
		User user = this.mongoTemplate.findById(new ObjectId(juser.getId()), User.class);
		
		c.setLessonId(lid);
		c.setContent(content);
		c.setUsername(user.getUsername());
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
