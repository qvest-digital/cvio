package org.tarent.cvio.server;

import org.tarent.cvio.server.common.CVIOConfiguration;
import org.tarent.cvio.server.common.ESNodeManager;
import org.tarent.cvio.server.cv.CVResource;
import org.tarent.cvio.server.skill.SkillResource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

/**
 * This is the central starting point for the cvio service.
 * It is based on the dropwizard framework and does the basic initialisation, here.
 * @author smancke
  */
public class CVIO extends Service<CVIOConfiguration> {

	/**
	 * startup with the parameter java CVIO server config.yaml.
	 * 
	 * @param args the commandline args
	 */
    public static void main(final String[] args) {
    	try {
    		new CVIO().run(args);
    	} catch (Exception e) {
    		System.out.println("Error while startup"); //NOSONAR
    		e.printStackTrace(); //NOSONAR
    		System.exit(1);
    	}
    }

    @Override
    public void initialize(final Bootstrap<CVIOConfiguration> bootstrap) {
        bootstrap.setName("cvio server");
    }
    
    @Override
    public void run(final CVIOConfiguration configuration, final Environment environment) {
    	Injector injector = Guice.createInjector(new CVIOGuiceModule(configuration));
    	
    	environment.manage(injector.getInstance(ESNodeManager.class));
        environment.addResource(injector.getInstance(CVResource.class));
        environment.addResource(injector.getInstance(SkillResource.class));
        environment.addHealthCheck(new Health());
    }
}
