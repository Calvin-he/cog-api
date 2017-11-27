package com.cog.api.controller;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cog.api.CogApiProperties;
import com.cog.api.model.AbstractDocument;
import com.cog.api.model.Media;
import com.cog.api.model.Series;

public class BaseController<T extends AbstractDocument> {
	protected final Log logger = LogFactory.getLog(MediaController.class);
	
	private final Class<T> genericType;
	
	@Autowired
	protected MongoTemplate mongoTemplate;
	@Autowired
	protected CogApiProperties cogApiProperties;
	
	@SuppressWarnings("unchecked")
	public BaseController() {
		this.genericType = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseController.class);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("")
	public List<T> listAll() {
		List<T> seriesList= this.mongoTemplate.findAll(genericType);
		return seriesList;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/{id}")
	public T get(@PathVariable String id) {
		return this.mongoTemplate.findById(id, genericType);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("")
	public T create(@Valid @RequestBody T s, BindingResult result) {
		Assert.isNull(s.get_id(), "id should be null");
		if(result.hasErrors()) {
			throw new RuntimeException();
		}
		s = innerCreate(s);
		Date now = new Date();
		s.setCreated(now);
		s.setUpdated(now);
		this.mongoTemplate.save(s);
		return s;
	}
	
	protected T innerCreate(T t) {
		return t;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PutMapping("/{id}")
	public Series update(@PathVariable String id, @RequestBody Map<String, Object> updatedFields) {
		Map<String, Object> newFields = this.innerUpdate(id, updatedFields);
		Update update = new Update();
		newFields.forEach((k,v) -> update.set(k, v));
		update.set("updated", new Date());
		FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);
		Series s = this.mongoTemplate.findAndModify(this.queryOfById(id), update, options, Series.class);
		return s;
	}
	
	protected Map<String, Object> innerUpdate(String id, Map<String, Object> updatedFields) {	
		return updatedFields;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{id}")
	public void delete(@PathVariable String id) {
		this.mongoTemplate.remove(this.queryOfById(id), genericType);
	}
	
	protected Query queryOfById(String id) {
		return new Query(where("_id").is(id));
	}
	
	String findMediaPathById(String mediaId) {
		Media m = this.mongoTemplate.findById(new ObjectId(mediaId), Media.class);
		return m.getPath();
	}
}
