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
 * @author smancke
 */
public class CVIOGuiceModule extends AbstractModule {

    /**
     * the condiguration instance.
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

    @Override
    protected void configure() {

        bind(CVIOConfiguration.class).toInstance(configuration);

        bind(ESNodeManager.class).toInstance(
                new ESNodeManagerImpl(configuration));

        bind(CVResource.class);
        bind(CVDB.class).to(CVDBService.class);

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
