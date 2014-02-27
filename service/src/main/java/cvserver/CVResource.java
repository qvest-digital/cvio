package cvserver;

import com.google.common.base.Optional;
import com.yammer.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/cv")
@Produces(MediaType.APPLICATION_JSON)
public class CVResource {

    private final String elasticSearchServer;

    public CVResource(String elasticSearchServer) {
        this.elasticSearchServer = elasticSearchServer;
    }

    @GET
    @Timed
    public String sayHello(@QueryParam("name") Optional<String> name) {
        return "Hallo " + name.or("Sebastian");
    }
}
