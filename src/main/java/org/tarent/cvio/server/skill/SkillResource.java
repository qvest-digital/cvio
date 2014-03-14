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
import javax.ws.rs.core.Response.Status;

import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;

/**
 * REsource for management of the skills.
 * 
 * @author smancke
 */
@Path("/skill")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SkillResource {

    /**
     * the skill db.
     */
    private SkillDB skillDB;

    /**
     * Create the resource.
     * 
     * @param theSkillDB skill db implementation
     */
    @Inject
    public SkillResource(final SkillDB theSkillDB) {
        this.skillDB = theSkillDB;
    }

    /**
     * returns all skills.
     * 
     * @return list of skills
     */
    @GET()
    @Path("/skills")
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
    @Path("/skills/{id}")
    @Timed
    public Response getSkillByid(@PathParam("id") final String theId) {
        Skill skill = skillDB.getSkillById(theId);
        if (skill == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(skill).build();
    }

    /**
     * creates a skill.
     * 
     * @param newSkill the skill to create
     * @return http response object
     */
    @POST()
    @Path("/skills")
    @Timed
    public Response createSkill(final Skill newSkill) {
        if (skillDB.getSkillById(newSkill.getName()) != null) {
            return Response
                    .status(Status.CONFLICT)
                    .entity("Skill name does already exist "
                            + newSkill.getName()).build();
        }
        URI uri = null;
        try {
            uri = new URI("/bla/" + newSkill.getName());
        } catch (URISyntaxException e) {

            throw new RuntimeException(e);
        }
        skillDB.createSkill(newSkill);
        return Response.created(uri).build();
    }

}
