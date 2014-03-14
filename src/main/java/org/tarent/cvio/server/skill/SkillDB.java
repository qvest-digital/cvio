package org.tarent.cvio.server.skill;

import java.util.List;

/**
 * interface for management of skills.
 * 
 * @author smancke
 * 
 */
public interface SkillDB {

    /**
     * returns all skills.
     * 
     * @return list of skills.
     */
    List<Skill> getAllSkills();

    /**
     * returns a skill by name.
     * 
     * @param name name of the skill
     * @return the skill
     */
    Skill getSkillByName(String name);

    /**
     * creates a skill.
     * 
     * @param newSkill the new skill to create
     */
    void createSkill(Skill newSkill);

}
