package org.tarent.cvio.server;

import org.tarent.cvio.server.skill.SkillDB;
import org.tarent.cvio.server.skill.SkillDBService;
import org.tarent.cvio.server.skill.SkillResource;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class CVGuiceModule extends AbstractModule {

	private CVIOConfiguration configuration;

	public CVGuiceModule(CVIOConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	protected void configure() {
		
		bind(CVIOConfiguration.class).toInstance(configuration);
		bind(CVResource.class);
		bind(SkillResource.class);
		bind(SkillDB.class).to(SkillDBService.class);
	}

}
