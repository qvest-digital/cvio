package org.tarent.cvio.server.skill;

import java.util.List;

public interface SkillDB {
	
	List<Skill> getAllSkills();

	Skill getSkillByName(String name);

	void createSkill(Skill newSkill);

}
