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
     * @param id of the skill
     * @return the skill
     */
    Skill getSkillById(String id);

    /**
     * creates a skill.
     * 
     * @param newSkill the new skill to create
     * @return returns the skill id
     */
    String createSkill(Skill newSkill);

    /**
     * updates a skill.
     * 
     * @param updateSkill the new skill to update
     */
    void updateSkill(Skill updateSkill);
}
