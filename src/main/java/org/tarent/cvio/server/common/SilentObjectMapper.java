package org.tarent.cvio.server.common;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * Wrapper for the jackson ObjectMapper, which only throws runtime exceptions.
 * 
 * @author smancke
 * 
 */
public class SilentObjectMapper {

    /**
     * the jackson mapper.
     */
    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setDateFormat(new ISO8601DateFormat());
        mapper.registerModule(new JodaModule());
    }

    /**
     * Parses the Json string into an instance of type.
     * 
     * @param jsonString the json soure
     * @param type the type of the target object
     * @param <T> return type
     * @return returns an instance of type
     */
    public <T> T readValue(final String jsonString, final Class<T> type) {
        try {
            return mapper.readValue(jsonString, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Json string from the supplied object.
     * 
     * @param o the input
     * @return json string
     */
    public String writeValueAsString(final Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
