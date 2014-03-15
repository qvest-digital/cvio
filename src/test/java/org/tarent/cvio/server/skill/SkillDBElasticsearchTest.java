package org.tarent.cvio.server.skill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.junit.Test;

public class SkillDBElasticsearchTest extends EsTest {

    @Test
    public void createReadOne() {
        // given a fresh database
        SkillDBElasticsearch skillDB = new SkillDBElasticsearch(esNode());

        // when I create a Skill
        Skill newSkill = demoSkill();
        String id = skillDB.createSkill(newSkill);
        assertNotNull(id);

        // the I can retrieve it by its name later
        Skill retrievedSkill = skillDB.getSkillById(id);
        skillsEqual(newSkill, retrievedSkill);
    }

    @Test
    public void testList() {
        // given a fresh database
        SkillDBElasticsearch skillDB = new SkillDBElasticsearch(esNode());

        // when I create a Skill
        Skill newSkill = demoSkill();
        String id = skillDB.createSkill(newSkill);
        skillDB.createSkill(demoSkill());
        skillDB.createSkill(demoSkill());

        refreshIndexes();

        // the I can retrieve it by its name later
        List<Skill> retrievedSkills = skillDB.getAllSkills();

        assertEquals(3, retrievedSkills.size());

        Skill retrievedSkill = findSkillById(retrievedSkills, id);
        assertNotNull(retrievedSkill);
        skillsEqual(newSkill, retrievedSkill);
    }

    private Skill findSkillById(List<Skill> retrievedSkills, String id) {
        for (Skill skill : retrievedSkills)
            if (skill.getId().equals(id))
                return skill;
        return null;
    }

    private void skillsEqual(Skill newSkill, Skill retrievedSkill) {
        assertEquals(retrievedSkill.getCategory(), newSkill.getCategory());
        assertTrue(retrievedSkill.getCreationTime().isEqual(newSkill.getCreationTime()));
        assertEquals(retrievedSkill.getDescription(), newSkill.getDescription());
        assertEquals(retrievedSkill.getName(), newSkill.getName());
    }

    private Skill demoSkill() {
        Skill newSkill = new Skill();
        newSkill.setName("Java " + new Random().nextLong());
        newSkill.setCategory("prog");
        newSkill.setCreationDate(new DateTime());
        newSkill.setDescription("This is a skill item" + new Random().nextLong());
        return newSkill;
    }
}
