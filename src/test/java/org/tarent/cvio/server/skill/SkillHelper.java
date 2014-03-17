package org.tarent.cvio.server.skill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;

public class SkillHelper {

    static Skill findSkillById(List<Skill> retrievedSkills, String id) {
        for (Skill skill : retrievedSkills)
            if (skill.getId().equals(id))
                return skill;
        return null;
    }

    static void skillsEqual(Skill newSkill, Skill retrievedSkill) {
        assertEquals(retrievedSkill.getCategory(), newSkill.getCategory());
        assertTrue(retrievedSkill.getCreationTime().isEqual(newSkill.getCreationTime()));
        assertEquals(retrievedSkill.getDescription(), newSkill.getDescription());
        assertEquals(retrievedSkill.getName(), newSkill.getName());
    }

    public static Skill demoSkill(String id) {
        Skill newSkill = demoSkillWithoutId();
        newSkill.setId(id);
        return newSkill;
    }

    static Skill demoSkillWithoutId() {
        Skill newSkill = new Skill();
        newSkill.setName("Java " + new Random().nextLong());
        newSkill.setCategory("prog");
        newSkill.setCreationDate(new DateTime());
        newSkill.setDescription("This is a skill item" + new Random().nextLong());
        return newSkill;
    }

    public static List<Skill> skillList() {
        List<Skill> list = new ArrayList<Skill>();
        list.add(demoSkillWithoutId());
        list.add(demoSkillWithoutId());
        list.add(demoSkillWithoutId());
        return list;
    }

}
