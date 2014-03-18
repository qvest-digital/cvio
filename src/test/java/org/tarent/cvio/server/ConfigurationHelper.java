package org.tarent.cvio.server;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response;

import org.tarent.cvio.server.common.CVIOConfiguration;

public class ConfigurationHelper {

    static CVIOConfiguration cfg = new CVIOConfiguration() {

        @Override
        public String getUriPrefix() {
            return "http://example.org:8080/bla";
        }

        public int getDefaultEsFetchSize() {
            return 5;
        }
    };

    public static void locationHeaderIsValid(CVIOConfiguration aConfiguration,
            Response httpResponse, String suffix) {
        String locationHeader = httpResponse.getMetadata().get("Location")
                .get(0).toString();
        assertTrue(locationHeader.startsWith(aConfiguration.getUriPrefix()));
        assertTrue(locationHeader.endsWith(suffix));
    }

    public static CVIOConfiguration cfg() {
        return cfg;
    }
}
