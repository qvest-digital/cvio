
package cvserver;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class CVService extends Service<CVServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new CVService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CVServiceConfiguration> bootstrap) {
        bootstrap.setName("cvservice");
    }
    
    @Override
    public void run(CVServiceConfiguration configuration, Environment environment) {

        environment.addResource(new CVResource(configuration.getElasticSearchServer()));
        environment.addHealthCheck(new Health());

    }
}