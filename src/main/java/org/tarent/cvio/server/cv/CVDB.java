package org.tarent.cvio.server.cv;

import java.util.List;
import java.util.Map;

/**
 * Interface for the management of cvs.
 * 
 * @author smancke
 * 
 */
public interface CVDB {

    /**
     * Returns the CVs.
     * 
     * @param fields list of fields to return in the answer
     * @return List of Maps with the requested fields and a special field '_id'
     *         for the document id.
     */
    List<Map<String, Object>> getAllCVs(String[] fields);

    /**
     * Creates a CV with the supplied JSON-Document.
     * 
     * @param content the cv as json string
     * @return returns the id if the document
     */
    String createCV(String content);

    /**
     * Returns one cv by its id.
     * 
     * @param id the cv id
     * @return the CV as JSON String
     */
    String getCVById(String id);

    /**
     * Stores the new content of the cv.
     * 
     * @param content the cv as json string
     * 
     * @param id the cv id
     */
    void updateCV(String id, String content);
    
    
    /**
     * Deltes a cv by id
     * @param id the cv id
     */
    void deleteCV(String id);
}
