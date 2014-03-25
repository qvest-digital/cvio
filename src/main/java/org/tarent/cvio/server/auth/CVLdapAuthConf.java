package org.tarent.cvio.server.auth;
import java.net.URI;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.tarent.cvio.server.common.CVIOConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.collect.Sets;
import com.yammer.dropwizard.authenticator.LdapConfiguration;
import com.yammer.dropwizard.util.Duration;

public class CVLdapAuthConf extends LdapConfiguration{
	
	  	@NotNull
	    @JsonProperty
	    private URI uri = URI.create("ldaps://myldap.com:636");

	    @NotNull
	    @JsonProperty
	    private CacheBuilderSpec cachePolicy = CacheBuilderSpec.disableCaching();

	    @NotNull
	    @NotEmpty
	    @JsonProperty
	    private String userFilter = "ou=people,dc=yourcompany,dc=com";

	    @NotNull
	    @NotEmpty
	    @JsonProperty
	    private String groupFilter = "ou=groups,dc=yourcompany,dc=com";

	    @NotNull
	    @NotEmpty
	    @JsonProperty
	    private String userNameAttribute = "cn";

	    @NotNull
	    @NotEmpty
	    @JsonProperty
	    private String groupNameAttribute = "cn";

	    @NotNull
	    @NotEmpty
	    @JsonProperty
	    private String groupMembershipAttribute = "memberUid";

	    @NotNull
	    @Valid
	    @JsonProperty
	    private Duration connectTimeout = Duration.milliseconds(500);

	    @NotNull
	    @Valid
	    @JsonProperty
	    private Duration readTimeout = Duration.milliseconds(500);

	    @NotNull
	    @Valid
	    @JsonProperty
	    private Set<String> restrictToGroups = Sets.newHashSet();

	    public URI getUri() {
	        return uri;
	    }

	    public CVLdapAuthConf setUri(URI uri) {
	        this.uri = uri;
	        return this;
	    }

	    public CacheBuilderSpec getCachePolicy() {
	        return cachePolicy;
	    }

	    public CVLdapAuthConf setCachePolicy(CacheBuilderSpec cachePolicy) {
	        this.cachePolicy = cachePolicy;
	        return this;
	    }

	    public String getUserFilter() {
	        return userFilter;
	    }

	    public CVLdapAuthConf setUserFilter(String userFilter) {
	        this.userFilter = userFilter;
	        return this;
	    }

	    public String getGroupFilter() {
	        return groupFilter;
	    }

	    public CVLdapAuthConf setGroupFilter(String groupFilter) {
	        this.groupFilter = groupFilter;
	        return this;
	    }

	    public String getUserNameAttribute() {
	        return userNameAttribute;
	    }

	    public CVLdapAuthConf setUserNameAttribute(String userNameAttribute) {
	        this.userNameAttribute = userNameAttribute;
	        return this;
	    }

	    public String getGroupNameAttribute() {
	        return groupNameAttribute;
	    }

	    public CVLdapAuthConf setGroupNameAttribute(String groupNameAttribute) {
	        this.groupNameAttribute = groupNameAttribute;
	        return this;
	    }

	    public String getGroupMembershipAttribute() {
	        return groupMembershipAttribute;
	    }

	    public CVLdapAuthConf setGroupMembershipAttribute(String groupMembershipAttribute) {
	        this.groupMembershipAttribute = groupMembershipAttribute;
	        return this;
	    }

	    public Duration getConnectTimeout() {
	        return connectTimeout;
	    }

	    public CVLdapAuthConf setConnectTimeout(Duration connectTimeout) {
	        this.connectTimeout = connectTimeout;
	        return this;
	    }

	    public Duration getReadTimeout() {
	        return readTimeout;
	    }

	    public CVLdapAuthConf setReadTimeout(Duration readTimeout) {
	        this.readTimeout = readTimeout;
	        return this;
	    }

	    public Set<String> getRestrictToGroups() {
	        return restrictToGroups;
	    }

	    public CVLdapAuthConf addRestrictedGroup(String group) {
	        restrictToGroups.add(group);
	        return this;
	    }
	    
}
