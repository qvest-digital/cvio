package org.tarent.cvio.server.cv;

import static org.junit.Assert.*;

import java.net.URISyntaxException;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.tarent.cvio.server.CVIOConfiguration;

public class CVResourceTest {

	private static final String DEMO_JSON = "{'demo': 'data'}";

	@Mock
	CVDB dbMock;
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test() throws URISyntaxException {
		
		// given
		CVIOConfiguration aConfiguration = getConfiguration();
		CVResource aRessource = new CVResource(dbMock, aConfiguration);
		Mockito.when(dbMock.createCV(DEMO_JSON)).thenReturn("THE-CV-ID");
		
		// when creating a new CV
		Response httpResponse = aRessource.createCV(DEMO_JSON);
		
		// then 
		// cv was created in the database 
		Mockito.verify(dbMock).createCV(DEMO_JSON);

		// and status code == created 
		assertEquals(201, httpResponse.getStatus());
		
		// and the location header
		String locationHeader = httpResponse.getMetadata().get("Location").get(0).toString();		
		assertTrue(  locationHeader.startsWith(aConfiguration.getUriPrefix())  );
		assertTrue(  locationHeader.endsWith("THE-CV-ID") );
		
	}

	private CVIOConfiguration getConfiguration() {
		return new CVIOConfiguration() {
			@Override
			public String getUriPrefix() {
				return "http://example.org:8080/bla";
			}
		};
	}

}
