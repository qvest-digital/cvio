package org.tarent.cvio.server;

import com.yammer.dropwizard.config.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

public class CVIOConfiguration extends Configuration {

	@NotEmpty
	@JsonProperty
	private String databasePath;

	/**
	 * Path to the folder of the database.
	 */
	public String getDatabasePath() {
		return databasePath;
	}

	@NotEmpty
	@JsonProperty
	private String uriPrefix;

	/**
	 * The prefix to use in rest uris. Does not contain a trailing '/'.
	 */
	public String getUriPrefix() {
		return uriPrefix;
	}
}