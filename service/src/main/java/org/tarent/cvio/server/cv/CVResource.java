package org.tarent.cvio.server.cv;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.tarent.cvio.server.CVIOConfiguration;

@Path("/cv")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CVResource {

    private CVDB cvdb;
	private CVIOConfiguration cfg;

	@Inject
    public CVResource(CVDB cvdb, CVIOConfiguration cfg) {
    	this.cvdb = cvdb;
    	this.cfg = cfg;
    }

    @GET
    @Path("/cvs")
    @Timed
    /**
     * Returns all CVs within the system
     * @return Response Object with the list of CVs
     */
    public List<Map<String,String>> getCVs(@QueryParam("fields") List<String> fields) {
        List<Map<String,String>> cvs = cvdb.getAllCVs(fields.toArray(new String[0]));
        for (Map<String,String> entry: cvs) {
        	entry.put("ref", cfg.getUriPrefix() + "/cv/cvs/"+ entry.get("id"));
        }
        return cvs;
    }

    @GET
    @Path("/cvs/{id}")
    @Timed
    /**
     * Returns one CV by the supplied id.
     * @return Returns the cv as json string.
     */
    public String getCV(@PathParam("id") String id) {
        return cvdb.getCVById(id);
    }    

    @PUT
    @Path("/cvs/{id}")
    @Timed
    /**
     * Overwrite a CV
     * @return Response Object with the status
     */
    public Response updateCV(@PathParam("id") String id, String content) {
        cvdb.updateCV(id, content);
        return Response.ok().build();
    }    

    @POST
    @Path("/cvs")
    @Timed
    /**
     * Creates a new CV 
     * @return Response Object with the status of the action
     */    
    public Response createCV(String content) throws URISyntaxException {
    	//Use JsonNode for json handling, if needed
    	String cvid = cvdb.createCV(content);
    	return Response.created(new URI(cfg.getUriPrefix() + "/cv/cvs/"+ cvid)).build();
	}   
}
