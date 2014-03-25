package org.tarent.cvio.server;

import org.tarent.cvio.server.auth.CVLdapAuth;
import org.tarent.cvio.server.auth.CVLdapAuthConf;
import org.tarent.cvio.server.common.CVIOConfiguration;
import org.tarent.cvio.server.common.ESNodeManager;
import org.tarent.cvio.server.common.ESNodeManagerImpl;
import org.tarent.cvio.server.cv.CVDB;
import org.tarent.cvio.server.cv.CVDBElasticsearch;
import org.tarent.cvio.server.cv.CVResource;
import org.tarent.cvio.server.skill.SkillDB;
import org.tarent.cvio.server.skill.SkillDBElasticsearch;
import org.tarent.cvio.server.skill.SkillResource;

import com.google.inject.AbstractModule;

/**
 * This is the central configuration for the dependency injection.
 * 
 * @see https://code.google.com/p/google-guice/
 * 
 * @author smancke
 */
public class CVIOGuiceModule extends AbstractModule {

    /**
     * the configuration instance.
     */
    private CVIOConfiguration configuration;

    /**
     * creates an instance of the guice configuration.
     * 
     * @param cfg the global configuration
     */
    public CVIOGuiceModule(final CVIOConfiguration cfg) {
        this.configuration = cfg;
    }

    /**
     * Wire everything up.
     */
    @Override
    protected void configure() {

        // The configuration
        bind(CVIOConfiguration.class).toInstance(configuration);

        // The Elasticsearch manager
        bind(ESNodeManager.class).to(ESNodeManagerImpl.class).asEagerSingleton();

        // Our /cv ressource and the corresponding database
        bind(CVResource.class);
        bind(CVDB.class).to(CVDBElasticsearch.class);

        // The /skill resource
        bind(SkillResource.class);
        bind(SkillDB.class).to(SkillDBElasticsearch.class);
        
        // the auth resource
        //bind(CVLdapAuthConf.class).toInstance(ldapconf);
        bind(CVLdapAuth.class);
    }
}
