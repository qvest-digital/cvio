package org.tarent.cvio.server.common;

import org.elasticsearch.client.Client;

import com.yammer.dropwizard.lifecycle.Managed;

/**
 * Interface for providing access to the elasticsearch client.
 * 
 * @author smancke
 */
public interface ESNodeManager extends Managed {

    /**
     * returns an es client instance.
     * 
     * @return the client
     */
    Client client();

    /**
     * Tests, if the supplied index exist.
     * 
     * @param index the index to test
     * @return true, if the index exsts
     */
    boolean doesIndexExist(String index);

}
