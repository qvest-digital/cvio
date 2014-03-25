package org.tarent.cvio.server.skill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tarent.cvio.server.ConfigurationHelper;

public class SkillResourceTest {

    @Mock
    SkillDB dbMock = null;

    SkillResource aResource = null;

    private List<Skill> skillList = SkillHelper.skillList();

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        aResource = new SkillResource(dbMock, ConfigurationHelper.cfg());
    }

    @Test
    public void testGetAll() {

        // given a list of skills
        when(dbMock.getAllSkills()).thenReturn(skillList);

        // then the list is returned
        List<Skill> result = aResource.getAllSkills(true);
        assertEquals(skillList.size(), result.size());
    }

    @Test
    public void testGetSkillById() {

        Skill newSkill = SkillHelper.demoSkill("idxx");

        // given a skill
        when(dbMock.getSkillById("idxx")).thenReturn(newSkill);

        // then the it is returned
        Skill retrievedSkill = aResource.getSkillByid("idxx", true);
        SkillHelper.skillsEqualWitoutId(newSkill, retrievedSkill);
    }

    @Test
    public void testGetSkillNotFound() {

        // given no skill
        when(dbMock.getSkillById("idxx")).thenReturn(null);

        // then the it is returned
        assertNull(aResource.getSkillByid("idxx", true));
    }

    @Test
    public void testCreateEntity() throws URISyntaxException {

        // given a skill
        Skill newSkill = SkillHelper.demoSkillWithoutId();
        when(dbMock.createSkill(newSkill)).thenReturn("anyID");

        // when skill is created
        Response httpResponse = aResource.createSkill(newSkill, true);

        // then database get called to persist the skill
        verify(dbMock, times(1)).createSkill(newSkill);

        // and status code == created
        assertEquals(201, httpResponse.getStatus());

        // and the location header is correct
        ConfigurationHelper.locationHeaderIsValid(
                ConfigurationHelper.cfg(),
                httpResponse,
                "anyID");
    }
}