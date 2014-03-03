package org.tarent.cvio.server.skill;

import org.joda.time.DateTime;

public class Skill {
	
	private String name;
	private String description;
	private String category;
	private DateTime creationTime;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public DateTime getCreationTime() {
		return creationTime;
	}
	public void setCreationDate(DateTime dateTime) {
		this.creationTime = dateTime;
	}
}
