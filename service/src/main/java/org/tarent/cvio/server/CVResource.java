package org.tarent.cvio.server;

import com.google.common.base.Optional;
import com.google.inject.Inject;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/cv")
@Produces(MediaType.APPLICATION_JSON)
public class CVResource {


    @Inject
    public CVResource(CVIOConfiguration cfg) {
    }

    @GET
    @Timed
    public String sayHello(@QueryParam("name") Optional<String> name) {
        return "Hallo " + name.or("Sebastian");
    }
}
