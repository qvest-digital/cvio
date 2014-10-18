package org.tarent.cvio.server.cv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.tarent.cvio.server.ConfigurationHelper;
import org.tarent.cvio.server.common.CVIOConfiguration;

public class CVResourceTest {

    private static final String CV_ID = "THE-CV-ID";

    private static final String DEMO_JSON = "{'demo': 'data'}";

    @Mock
    CVDB dbMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCV() throws URISyntaxException {

        // given
        CVIOConfiguration aConfiguration = ConfigurationHelper.cfg();
        CVResource aRessource = new CVResource(dbMock, aConfiguration);
        Mockito.when(dbMock.createCV(DEMO_JSON)).thenReturn(CV_ID);

        // when creating a new CV
        Response httpResponse = aRessource.createCV(DEMO_JSON, true);

        // then
        // cv was created in the database
        Mockito.verify(dbMock).createCV(DEMO_JSON);

        // and status code == created
        assertEquals(201, httpResponse.getStatus());

        // and the location header
        ConfigurationHelper.locationHeaderIsValid(aConfiguration, httpResponse, CV_ID);
    }

    @Test
    public void getCV() {
        // given
        CVIOConfiguration aConfiguration = ConfigurationHelper.cfg();
        CVResource aRessource = new CVResource(dbMock, aConfiguration);
        Mockito.when(dbMock.getCVById(CV_ID)).thenReturn(DEMO_JSON);

        // when I request a CV
        String result = aRessource.getCV(CV_ID, true);

        // then
        assertEquals(DEMO_JSON, result);
    }

    @Test
    public void getAllCVs() {
        // given
        CVIOConfiguration aConfiguration = ConfigurationHelper.cfg();
        CVResource aRessource = new CVResource(dbMock, aConfiguration);
        String[] someFields = { "demo" };

        List<Map<String, Object>> demoResultData = demoResultData();
        Mockito.when(dbMock.getCVs(someFields, null)).thenReturn(demoResultData);

        // when I request a CV
        List<Map<String, Object>> result = aRessource.getCVs(Arrays
                .asList(someFields), null, true);

        // then
        // data matches:
        assertEquals("Alice", result.get(0).get("name"));
        assertEquals("Bob", result.get(1).get("name"));

        // and entries have a valid uri ref
        assertNotNull(result.get(0).get("ref"));
        assertTrue(result.get(0).get("ref").toString()
                .startsWith(aConfiguration.getUriPrefix()));

        assertNotNull(result.get(1).get("ref"));
        assertTrue(result.get(1).get("ref").toString()
                .startsWith(aConfiguration.getUriPrefix()));
    }

    private List<Map<String, Object>> demoResultData() {
        return new ArrayList<Map<String, Object>>() {
            {
                ;
                add(new HashMap<String, Object>() {
                    {
                        put("demo", "data");
                        put("name", "Alice");
                    }
                });
                add(new HashMap<String, Object>() {
                    {
                        put("demo", "data");
                        put("name", "Bob");
                    }
                });
            }
        };
    }
}
