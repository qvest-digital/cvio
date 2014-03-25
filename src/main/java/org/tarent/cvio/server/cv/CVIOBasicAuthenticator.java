package org.tarent.cvio.server.cv;

import org.tarent.cvio.server.common.CVIOConfiguration;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;

/**
 * Basic autentificator for accessing resources.
 * 
 * @author sreime
 * @author vhamm
 *
 */
public class CVIOBasicAuthenticator implements Authenticator<BasicCredentials, Boolean> {

	private CVIOConfiguration conf;
	
	public CVIOBasicAuthenticator(CVIOConfiguration conf) {
		this.conf = conf;
	}
	
	/***
	 * Authentification with credentials.
	 */
	public Optional<Boolean> authenticate(BasicCredentials c) throws AuthenticationException{
		if (c.getUsername().equals(conf.getDefaultUser()) &&
				c.getPassword().equals(conf.getDefaultPW())){
			return Optional.of(true);
		}
		return Optional.absent();
	}
	
}
