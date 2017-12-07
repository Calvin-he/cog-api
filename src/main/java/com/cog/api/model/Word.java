package com.cog.api.model;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="words")
public class Word extends AbstractDocument {

	private String word;
	private List<Pronoun> pronouns;
	private Map<String, Object> _forvoResults;
	private String phonetic_uk;
	private String phonetic_us;
	
			
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}


	public List<Pronoun> getPronouns() {
		return pronouns;
	}


	public void setPronouns(List<Pronoun> pronouns) {
		this.pronouns = pronouns;
	}


	public Map<String, Object> get_forvoResults() {
		return _forvoResults;
	}


	public void set_forvoResults(Map<String, Object> _forvoResults) {
		this._forvoResults = _forvoResults;
	}


	public String getPhonetic_uk() {
		return phonetic_uk;
	}

	public void setPhonetic_uk(String phonetic_uk) {
		this.phonetic_uk = phonetic_uk;
	}

	public String getPhonetic_us() {
		return phonetic_us;
	}

	public void setPhonetic_us(String phonetic_us) {
		this.phonetic_us = phonetic_us;
	}


	public static class Pronoun {
		private String audioPath;
		private String countryOfReader;
		private String sexOfReader; // m: male; f: femail
		public String getAudioPath() {
			return audioPath;
		}
		public void setAudioPath(String audioPath) {
			this.audioPath = audioPath;
		}
		public String getCountryOfReader() {
			return countryOfReader;
		}
		public void setCountryOfReader(String countryOfReader) {
			this.countryOfReader = countryOfReader;
		}
		public String getSexOfReader() {
			return sexOfReader;
		}
		public void setSexOfReader(String sexOfReader) {
			this.sexOfReader = sexOfReader;
		}
		
	}
}


