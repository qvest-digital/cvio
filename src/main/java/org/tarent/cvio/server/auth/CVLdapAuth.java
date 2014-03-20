package org.tarent.cvio.server.auth;

import java.io.Console;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import com.yammer.dropwizard.authenticator.LdapAuthenticator;
import com.yammer.metrics.annotation.Timed;


@Path("/login/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CVLdapAuth {
	
	@Timed
    @POST
	public String userForAuth(final String content) throws URISyntaxException {
		System.out.println(content);		
		String name = null;
		String pw = null;
	        
	        try {
				JSONObject json = (JSONObject)new JSONParser().parse(content);
				name = (String) json.get("name");
				pw = (String) json.get("passwort");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        /*
	    	try {
				authenticator.authenticate(new BasicCredentials(name, pw));
			} catch (AuthenticationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		
		
		
		return content;
	}
}