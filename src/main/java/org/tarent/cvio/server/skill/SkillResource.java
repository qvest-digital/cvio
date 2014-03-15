package org.tarent.cvio.server.skill;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.tarent.cvio.server.common.CVIOConfiguration;

import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;

/**
 * REsource for management of the skills.
 * 
 * @author smancke
 */
@Path("/skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SkillResource {

    /**
     * the skill db.
     */
    private SkillDB skillDB;

    /**
     * the global configuration.
     */
    private CVIOConfiguration configuration;

    /**
     * Create the resource.
     * 
     * @param theSkillDB skill db implementation
     * @param cvioConfiguration
     */
    @Inject
    public SkillResource(final SkillDB theSkillDB, CVIOConfiguration theConfiguration) {
        this.skillDB = theSkillDB;
        this.configuration = theConfiguration;
    }

    /**
     * returns all skills.
     * 
     * @return list of skills
     */
    @GET()
    @Timed
    public List<Skill> getAllSkills() {
        return skillDB.getAllSkills();
    }

    /**
     * Returns a skill by its id.
     * 
     * @param theId the skill id
     * @return the skill
     */
    @GET()
    @Path("/{id}")
    @Timed
    public Skill getSkillByid(@PathParam("id") final String theId) {
        Skill skill = skillDB.getSkillById(theId);
        System.out.println("skill: " + skill);
        return skill;
    }

    /**
     * creates a skill.
     * 
     * @param newSkill the skill to create
     * @return http response object
     * @throws URISyntaxException in case of a miss configuration
     */
    @POST()
    @Timed
    public Response createSkill(final Skill newSkill) throws URISyntaxException {
        String id = skillDB.createSkill(newSkill);
        return Response
                .created(new URI(configuration.getUriPrefix() + "/skills/" + id))
                .build();
    }

}
