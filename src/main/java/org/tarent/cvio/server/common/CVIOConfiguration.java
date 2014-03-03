package org.tarent.cvio.server.common;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

/**
 * This ist the application configuration for cvio.
 * 
 * @author smancke
 */
public class CVIOConfiguration extends Configuration {

    /**
     * The path to the database folder.
     */
    @NotEmpty
    @JsonProperty
    private String dataDirectory;

    /**
     * The prefix to use in rest uris. Does not contain a trailing '/'.
     */
    @NotEmpty
    @JsonProperty
    private String uriPrefix;

    /**
     * to which port addess shoudl eslasticsearch bind, if http is enabled (e.g.
     * 127.0.0.1, 0.0.0.0)?
     */
    @NotEmpty
    @JsonProperty
    private String elasticsearchBindHost;

    /**
     * Should the embedded elasticseach open an http port?
     */
    @NotEmpty
    @JsonProperty
    private String elasticsearchEnableHttp;

    /**
     * Path to the folder of the database.
     * 
     * @return the path
     */
    public String getDataDirectory() {
        return dataDirectory;
    }

    /**
     * The prefix to use in rest uris. Does not contain a trailing '/'.
     * 
     * @return the uri prefix
     */
    public String getUriPrefix() {
        return uriPrefix;
    }

    /**
     * to which port addess shoudl eslasticsearch bind, if http is enabled (e.g.
     * 127.0.0.1, 0.0.0.0)?
     * 
     * @return the address to bind
     */
    public String getElasticsearchBindHost() {
        return elasticsearchBindHost;
    }

    /**
     * Should the embedded elasticseach open an http port?
     * 
     * @return true or false
     */
    public boolean isElasticsearchEnableHttp() {
        return new Boolean(elasticsearchEnableHttp);
    }
}
