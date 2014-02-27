package org.tarent.cvio.server.skill;

import java.util.List;

public interface SkillDB {
	
	public List<Skill> getAllSkills();

	public Skill getSkillByName(String name);

	public void createSkill(Skill newSkill);

}
