package org.tarent.cvio.server.cv;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;

public class authenticator implements Authenticator<BasicCredentials, Boolean> {
	public Optional<Boolean> authenticate(BasicCredentials c) throws AuthenticationException{
		// TODO add user and password to config
		if (c.getUsername().equals("placeholder") &&
				c.getPassword().equals("placeholder")){
			return Optional.of(true);
		}
		return Optional.absent();
	}
}
