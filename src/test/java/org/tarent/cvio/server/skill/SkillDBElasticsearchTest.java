package org.tarent.cvio.server.skill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

    private void skillsEqual(Skill newSkill, Skill retrievedSkill) {
        assertEquals(retrievedSkill.getCategory(), newSkill.getCategory());
        assertTrue(retrievedSkill.getCreationTime().isEqual(
                newSkill.getCreationTime()));
        assertEquals(retrievedSkill.getDescription(), newSkill.getDescription());
        assertEquals(retrievedSkill.getName(), newSkill.getName());
    }

    private Skill demoSkill() {
        Skill newSkill = new Skill();
        newSkill.setCategory("prog");
        newSkill.setCreationDate(new DateTime());
        newSkill.setDescription("This is a skill item");
        newSkill.setName("Java " + new Random().nextLong());
        return newSkill;
    }
}
