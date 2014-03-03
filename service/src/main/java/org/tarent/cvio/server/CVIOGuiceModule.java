package org.tarent.cvio.server;

import java.util.List;

import org.tarent.cvio.server.common.CVIOConfiguration;
import org.tarent.cvio.server.cv.CVDB;
import org.tarent.cvio.server.cv.CVDBService;
import org.tarent.cvio.server.cv.CVResource;
import org.tarent.cvio.server.skill.Skill;
import org.tarent.cvio.server.skill.SkillDB;
import org.tarent.cvio.server.skill.SkillResource;

import com.google.inject.AbstractModule;

public class CVIOGuiceModule extends AbstractModule {

	private CVIOConfiguration configuration;

	public CVIOGuiceModule(CVIOConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	protected void configure() {
		
		bind(CVIOConfiguration.class).toInstance(configuration);

		bind(CVResource.class);
		bind(CVDB.class).to(CVDBService.class);
		
		bind(SkillResource.class);
		bind(SkillDB.class).toInstance(new SkillDB() {
			public List<Skill> getAllSkills() {
				return null;
			}
			public Skill getSkillByName(String name) {
				return null;
			}
			public void createSkill(Skill newSkill) {
			}
		});
	}

}
