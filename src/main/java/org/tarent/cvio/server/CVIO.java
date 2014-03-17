package org.tarent.cvio.server;

import org.tarent.cvio.server.common.CVIOConfiguration;
import org.tarent.cvio.server.common.ESNodeManager;
import org.tarent.cvio.server.cv.CVResource;
import org.tarent.cvio.server.skill.SkillResource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.CachingAuthenticator;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import com.yammer.dropwizard.authenticator.LdapAuthenticator;
import com.yammer.dropwizard.authenticator.LdapCanAuthenticate;
import com.yammer.dropwizard.authenticator.LdapConfiguration;
import com.yammer.dropwizard.authenticator.ResourceAuthenticator;
import com.yammer.dropwizard.authenticator.healthchecks.LdapHealthCheck;
import com.yammer.dropwizard.config.Configuration;



/**
 * This is the central starting point for the cvio service. It is based on the
 * dropwizard framework and does the basic initialisation, here.
 * 
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
            System.out.println("Error while startup"); // NOSONAR
            e.printStackTrace(); // NOSONAR
            System.exit(1);
        }
    }

    /**
     * Initialisation of the service.
     * 
     * @param bootstrap The Dropwizard Bootstrap Instance
     */
    @Override
    public void initialize(final Bootstrap<CVIOConfiguration> bootstrap) {
        bootstrap.setName("cvio server");

        /**
         * The Assets Bundle enables surfing of static web from the /webroot
         * directory with the classpath, provides in the / root directory of the
         * service
         */
        bootstrap.addBundle(new AssetsBundle("/webroot/", "/"));
    }

    /**
     * The run method does the essential service configuration. Here we add all
     * Resources, Management Classes and HealthChecks.
     * 
     * @param configuration the configuration
     * @param environment the dropwizard environment
     */
    @Override
    public void run(final CVIOConfiguration configuration,
            final Environment environment) throws Exception {
    
    	// ldap config
    	LdapConfiguration conf = new LdapConfiguration();
    	LdapAuthenticator authenticator = new LdapAuthenticator(conf);
    	//authenticator.authenticate(new BasicCredentials("user", "password"));
    	

        // We are using Googe Guice for creating and wiring of our instances
        // see https://code.google.com/p/google-guice/
        Injector injector = Guice.createInjector(new CVIOGuiceModule(
                configuration));

        // Elasticsearch Manager
        environment.manage(injector.getInstance(ESNodeManager.class));

        // Our Resou rces
        environment.addResource(injector.getInstance(CVResource.class));
        environment.addResource(injector.getInstance(SkillResource.class));

        // An example HealthCheck
        environment.addHealthCheck(new Health());
        
        
        // Ldap Auth
        LdapConfiguration ldapConfiguration = conf;
	    Authenticator<BasicCredentials, BasicCredentials> ldapAuthenticator =
	        CachingAuthenticator.wrap(
	            new ResourceAuthenticator(new LdapAuthenticator(ldapConfiguration)),
	            ldapConfiguration.getCachePolicy());

	    environment.addProvider(new BasicAuthProvider<>(ldapAuthenticator, "realm"));
	    environment.addHealthCheck(new LdapHealthCheck(new ResourceAuthenticator(new LdapCanAuthenticate(ldapConfiguration))));
	    
    }
    
}
