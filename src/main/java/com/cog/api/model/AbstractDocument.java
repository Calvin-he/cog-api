package com.cog.api.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

/**
 * Base class for document classes.
 * 
 */
public class AbstractDocument {

	@Id
	private String _id;
	private Date created = new Date();
	private Date updated = new Date();
	
	/**
	 * Returns the identifier of the document.
	 * 
	 * @return the id
	 */
	public String get_id() {
		return _id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}

		if (this._id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
			return false;
		}

		AbstractDocument that = (AbstractDocument) obj;

		return this._id.equals(that.get_id());
	}

	@Override
	public int hashCode() {
		return _id == null ? 0 : _id.hashCode();
	}
}