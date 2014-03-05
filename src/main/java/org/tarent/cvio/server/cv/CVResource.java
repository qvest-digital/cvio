package org.tarent.cvio.server.cv;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

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

import org.tarent.cvio.server.common.CVIOConfiguration;

import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;

/**
 * The cv resource encapsulates all management operations for the cv handling.
 * It does not provide further rights checking, so the enabled user has full
 * access to all the cvs.
 * 
 * @author smancke
 * 
 */
@Path("/cv")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CVResource {

    /**
     * The db implementation for the cvs.
     */
    private CVDB cvdb;

    /**
     * The global configuration.
     */
    private CVIOConfiguration configuration;

    /**
     * Creates a new CV Resource.
     * 
     * @param cvdbImpl The db implementation for the cvs.
     * @param cfg The global configuration.
     */
    @Inject
    public CVResource(final CVDB cvdbImpl, final CVIOConfiguration cfg) {
        this.cvdb = cvdbImpl;
        this.configuration = cfg;
    }

    /**
     * Returns all CVs within the system.
     * 
     * @param fields the fields of the cv object, which should be returned
     * @return Response Object with the list of CVs
     */
    @Timed
    @GET
    @Path("/cvs")
    public List<Map<String, String>> getCVs(
            @QueryParam("fields") final List<String> fields) {
        List<Map<String, String>> cvs = cvdb.getAllCVs(fields
                .toArray(new String[0]));
        for (Map<String, String> entry : cvs) {
            entry.put("ref",
                    configuration.getUriPrefix() + "/cv/cvs/" + entry.get("id"));
        }
        return cvs;
    }

    /**
     * Returns one CV by the supplied id.
     * 
     * @param id the id of the cv
     * @return Returns the cv as json string.
     */
    @Timed
    @GET
    @Path("/cvs/{id}")
    public String getCV(@PathParam("id") final String id) {
        return cvdb.getCVById(id);
    }

    /**
     * Overwrite a CV.
     * 
     * @param id the id of the cv
     * @param content the json document of the cv as string
     * @return Response Object with the status
     */
    @Timed
    @PUT()
    @Path("/cvs/{id}")
    public Response updateCV(@PathParam("id") final String id,
            final String content) {
        cvdb.updateCV(id, content);
        return Response.ok().build();
    }

    /**
     * Creates a new CV.
     * 
     * @param content the json document of the cv as string
     * @return Response Object with the status of the action
     * @throws URISyntaxException may only occur in cases of miss configuration
     *             of the uri prefix
     */
    @Timed
    @POST
    @Path("/cvs")
    public Response createCV(final String content) throws URISyntaxException {
        // Use JsonNode for json handling, if needed
        String cvid = cvdb.createCV(content);
        return Response
                .created(
                        new URI(configuration.getUriPrefix() + "/cv/cvs/"
                                + cvid))
                .build();
    }
}
