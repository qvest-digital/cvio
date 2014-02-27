package org.tarent.cvio.server.skill;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/skill")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SkillResource {

    private SkillDB skillDB;

    @Inject
	public SkillResource(SkillDB skillDB) {
        this.skillDB = skillDB;
    }

    @GET()
    @Path("/skills")
    @Timed
    public List<Skill> getAllSkills() {
    		return skillDB.getAllSkills();
    }

    @GET()
    @Path("/skills/{name}")
    @Timed
    public Response getSkillByName(@PathParam("name") String name) {
		Skill skill = skillDB.getSkillByName(name);
		if (skill == null)
			return Response.status(Status.NOT_FOUND).build();
		return Response.ok(skill).build();
    }

    @POST()
    @Path("/skills")
    @Timed
    public Response createSkill(Skill newSkill) {
    	if (skillDB.getSkillByName(newSkill.getName()) != null)
    		return Response.status(Status.CONFLICT).entity("Skill name does already exist "+newSkill.getName()).build();
    	URI uri = null;
		try {
			uri = new URI("/bla/"+ newSkill.getName());
		} catch (URISyntaxException e) {
		}
    	skillDB.createSkill(newSkill);
    	return Response.created(uri).build();
    }
        
}
