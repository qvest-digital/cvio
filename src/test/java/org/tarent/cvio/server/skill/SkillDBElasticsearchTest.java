package org.tarent.cvio.server.skill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.tarent.cvio.server.EsTest;

public class SkillDBElasticsearchTest extends EsTest {

    @Test
    public void createReadOne() {
        // given a fresh database
        SkillDBElasticsearch skillDB = new SkillDBElasticsearch(esNode());

        // when I create a Skill
        Skill newSkill = SkillHelper.demoSkillWithoutId();
        String id = skillDB.createSkill(newSkill);
        assertNotNull(id);

        // then I can retrieve it by its name later
        Skill retrievedSkill = skillDB.getSkillById(id);
        assertNotNull(retrievedSkill.getId());
        SkillHelper.skillsEqualWitoutId(newSkill, retrievedSkill);
    }

    @Test
    public void testList() {
        // given a fresh database
        SkillDBElasticsearch skillDB = new SkillDBElasticsearch(esNode());

        // when I create a Skill
        Skill newSkill = SkillHelper.demoSkillWithoutId();
        String id = skillDB.createSkill(newSkill);
        skillDB.createSkill(SkillHelper.demoSkillWithoutId());
        skillDB.createSkill(SkillHelper.demoSkillWithoutId());

        refreshIndexes();

        // then I can retrieve the list later
        List<Skill> retrievedSkills = skillDB.getAllSkills();

        assertEquals(3, retrievedSkills.size());

        Skill retrievedSkill = SkillHelper.findSkillById(retrievedSkills, id);
        assertNotNull(retrievedSkill);
        SkillHelper.skillsEqualWitoutId(newSkill, retrievedSkill);
        assertNotNull(retrievedSkill.getId());
    }

    @Test
    public void testNullValuesOnNonExistingIndexex() {
        // given a fresh database
        SkillDBElasticsearch skillDB = new SkillDBElasticsearch(esNode());

        assertEquals(0, skillDB.getAllSkills().size());
        assertNull(skillDB.getSkillById("foo"));
    }
}
