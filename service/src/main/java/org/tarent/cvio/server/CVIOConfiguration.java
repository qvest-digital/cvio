package org.tarent.cvio.server;

import com.yammer.dropwizard.config.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotEmpty;

public class CVIOConfiguration extends Configuration {
    
    @NotEmpty
    @JsonProperty
    private String databasePath;
    
	public String getDatabasePath() {
		return databasePath;
	}
}