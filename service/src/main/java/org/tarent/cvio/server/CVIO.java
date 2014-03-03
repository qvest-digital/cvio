
package org.tarent.cvio.server;

import org.tarent.cvio.server.common.CVIOConfiguration;
import org.tarent.cvio.server.cv.CVResource;
import org.tarent.cvio.server.skill.SkillResource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class CVIO extends Service<CVIOConfiguration> {

    public static void main(String[] args) {
    	try {
    		new CVIO().run(args);
    	} catch (Exception e) {
    		System.out.println("Error while startup");
    		e.printStackTrace();
    		System.exit(1);
    	}
    }

    @Override
    public void initialize(Bootstrap<CVIOConfiguration> bootstrap) {
        bootstrap.setName("cvio server");
    }
    
    @Override
    public void run(CVIOConfiguration configuration, Environment environment) {
    	Injector injector = Guice.createInjector(new CVIOGuiceModule(configuration));
    	
        environment.addResource(injector.getInstance(CVResource.class));
        environment.addResource(injector.getInstance(SkillResource.class));
        environment.addHealthCheck(new Health());
    }
}