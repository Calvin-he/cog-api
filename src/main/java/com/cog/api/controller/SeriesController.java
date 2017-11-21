package com.cog.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cog.api.model.Lesson;
import com.cog.api.model.Series;

@RestController
@RequestMapping("/series")
public class SeriesController extends BaseController<Series> {
	@Override
	protected Series innerCreate(Series s) {
		String path = this.findMediaPathById(s.getBannerId());
		s.setBannerPath(path);
		return s;
	}
	
	@Override
	protected Map<String, Object> innerUpdate(String id, Map<String, Object> updatedFields) {
		Map<String, Object> m  = new HashMap<String, Object>(updatedFields);
		String bannerId = (String)updatedFields.get("bannerId");
		if(!StringUtils.isEmpty(bannerId)) {
			m.put("bannerPath", this.findMediaPathById(bannerId));
		}
		return m;
	}
	
	@GetMapping("/{id}/lessons")
	public List<Lesson> listLesson(@PathVariable String id) {
		Series s = this.mongoTemplate.findById(new ObjectId(id), Series.class);
		Query q = new Query(Criteria.where("_id").in(s.getLessonList()));
		List<Lesson> lessons = this.mongoTemplate.find(q, Lesson.class);
		return lessons;
	}
	
	@GetMapping("/{id}/lessons/{lessonId}")
	public Lesson getLesson(@PathVariable String lessonId) {
		Lesson les = this.mongoTemplate.findById(new ObjectId(lessonId), Lesson.class);
		return les;
	}
	
	@PutMapping("/{id}/lessons/{lessonId}/visitlesson")
	public Lesson makeLessonVisited(@PathVariable String id, @PathVariable String lessonId) {
		throw new RuntimeException("Not implement yet!");
	}
}
