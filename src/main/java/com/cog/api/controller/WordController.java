package com.cog.api.controller;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cog.api.model.Word;

@RestController
@RequestMapping("/api/1.0/words")
public class WordController extends BaseController<Word> {
	@Override
	public List<Word> listAll() {
		Query query = new Query();
		query.fields().exclude("_forvoResults");
		List<Word> seriesList= this.mongoTemplate.find(query, Word.class);
		return seriesList;
	}
}
