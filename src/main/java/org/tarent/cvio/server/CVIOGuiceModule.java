package org.tarent.cvio.server;

import java.util.List;

import org.tarent.cvio.server.common.CVIOConfiguration;
import org.tarent.cvio.server.common.ESNodeManager;
import org.tarent.cvio.server.common.ESNodeManagerImpl;
import org.tarent.cvio.server.cv.CVDB;
import org.tarent.cvio.server.cv.CVDBService;
import org.tarent.cvio.server.cv.CVResource;
import org.tarent.cvio.server.skill.Skill;
import org.tarent.cvio.server.skill.SkillDB;
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
        bind(ESNodeManager.class).toInstance(
                new ESNodeManagerImpl(configuration));

        // Our /cv ressource and the corresponding database
        bind(CVResource.class);
        bind(CVDB.class).to(CVDBService.class);

        // The /skill resource
        // and a dummy db implementation
        bind(SkillResource.class);
        bind(SkillDB.class).toInstance(new SkillDB() {
            public List<Skill> getAllSkills() {
                return null;
            }

            public Skill getSkillByName(final String name) {
                return null;
            }

            public void createSkill(final Skill newSkill) {
            }
        });
    }
}
