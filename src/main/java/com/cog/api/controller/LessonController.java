package com.cog.api.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
		
		String mediaPath2 = les.getMediaId2();
		if(!StringUtils.isEmpty(mediaPath2)) {
			mediaPath2 = this.findMediaPathById(mediaPath2);
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
	public List<Comment> listComment(@PathVariable String id) {
		Query q = new Query(Criteria.where("lessonId").is(id));
		List<Comment> comments = this.mongoTemplate.find(q, Comment.class);
		Collections.reverse(comments);
		return comments;
	}
	
	@PostMapping("/{id}/comments")
	public Comment createComment(@PathVariable String id, @RequestBody Map<String, Object> body) {
		String content = (String)body.get("content");
		Comment c = new Comment();
		JwtUser juser = SecurityUtils.getCurrentUser();
		User user = this.mongoTemplate.findById(new ObjectId(juser.getId()), User.class);
		System.out.println("user id is " + juser.getId());
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
			query = new Query(Criteria.where("_id").is(commentId).andOperator(Criteria.where("userId").is(juser.getId())));
			this.mongoTemplate.remove(query, Comment.class);
		}
	}
	
	@PutMapping("{id}/action/inc-visited-count")
	public void incVisitedCount(@PathVariable String id) {
		Query query = new Query(Criteria.where("_id").is(id));
		this.mongoTemplate.updateFirst(query, new Update().inc("visitedCount", 1), Lesson.class);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("{id}/comments/{commentId}/reply")
	public Comment replayComment(@PathVariable String id, @PathVariable String commentId, @RequestBody Map<String, String> comment) {
		JwtUser juser = SecurityUtils.getCurrentUser();
		Comment reply = new Comment();
		reply.setContent(comment.get("content"));
		reply.setUserId(juser.getId());
		reply.setUserNickname(comment.get("userNickname"));
		reply.setUserAvatar(comment.get("userAvatar"));
		Query query = new Query(Criteria.where("_id").is(commentId));
		Update update = new Update().push("replies", reply);
		this.mongoTemplate.updateFirst(query, update, Comment.class);
		return reply;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("{id}/comments/{commentId}/reply/{index}")
	public void deleteCommentReply(@PathVariable String id, @PathVariable String commentId, 
			@PathVariable int index) {
		Query query = new Query(Criteria.where("_id").is(commentId));
		Comment comment = this.mongoTemplate.findOne(query, Comment.class);
		Comment[] replies = comment.getReplies();
		if(index < 0 || index > replies.length) {
			throw new RuntimeException("Invalid parameter of 'index'");
		}
		Comment[] newReplies= ArrayUtils.remove(replies, index);
		Update update = new Update().set("replies", newReplies);
		this.mongoTemplate.updateFirst(query, update, Comment.class);
	}
}
