package org.tarent.cvio.server.auth;

import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.tarent.cvio.server.common.CVIOConfiguration;

import com.google.inject.Inject;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import com.yammer.dropwizard.authenticator.LdapAuthenticator;
import com.yammer.metrics.annotation.Timed;


@Path("/login/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CVLdapAuth {
	
	
	// Test for ldap
	private CVIOConfiguration configuration;

	@Inject
	public CVLdapAuth(CVIOConfiguration conf) {
		this.configuration = conf;
	}
	
	@Timed
    @POST
	public String userForAuth(final String content) throws URISyntaxException {
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
	        
	    	try {
	    		LdapAuthenticator authenticator = new LdapAuthenticator(this.configuration.getLdapConf()); 
				authenticator.authenticate(new BasicCredentials(name, pw));
			} catch (AuthenticationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		return content;
	}
}
