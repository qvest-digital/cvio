package org.tarent.cvio.server.common;

import com.yammer.dropwizard.config.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * This ist the application configuration for cvio.
 * @author smancke
 */
public class CVIOConfiguration extends Configuration {

	/**
	 * The path to the database folder.
	 */
	@NotEmpty
	@JsonProperty
	private String databasePath;

	/**
	 * Path to the folder of the database.
	 * @return the path
	 */
	public String getDatabasePath() {
		return databasePath;
	}

	/**
	 *  The prefix to use in rest uris. Does not contain a trailing '/'.
	 */
	@NotEmpty
	@JsonProperty
	private String uriPrefix;

	/**
	 * The prefix to use in rest uris. Does not contain a trailing '/'.
	 * @return the uri prefix
	 */
	public String getUriPrefix() {
		return uriPrefix;
	}
}
