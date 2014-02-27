package cvserver;

import com.yammer.dropwizard.config.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class CVServiceConfiguration extends Configuration {
    
    @NotEmpty
    @JsonProperty
    private String elasticSearchServer;
    
    public String getElasticSearchServer() {
        return elasticSearchServer;
    }
}