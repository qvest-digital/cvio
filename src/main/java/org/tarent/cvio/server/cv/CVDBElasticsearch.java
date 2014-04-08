package org.tarent.cvio.server.cv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tarent.cvio.server.common.CVIOConfiguration;
import org.tarent.cvio.server.common.ESNodeManager;

import com.google.inject.Inject;

/**
 * Elasticsearch based implementation of the CVDB.
 * 
 * @author smancke
 */
public class CVDBElasticsearch implements CVDB {

    /**
     * the logger.
     */
    private final Logger logger = LoggerFactory
            .getLogger(CVDBElasticsearch.class);

    /**
     * The eslasticsearch type for CV documents.
     */
    private static final String TYPE_CV = "cv";

    /**
     * The elasticsearch index name fo cvs.
     */
    private static final String INDEX_CVS = "cvs";

    /**
     * The elasticsearch client manager instance.
     */
    private ESNodeManager es;

    /**
     * The global Configuration.
     */
    private CVIOConfiguration config;

    /**
     * Create a new CVDBService based on elastic search.
     * 
     * @param esNode the access to elasticsearch
     * @param theConfiguration The global Configuration
     */
    @Inject
    public CVDBElasticsearch(final ESNodeManager esNode, final CVIOConfiguration theConfiguration) {
        es = esNode;
        config = theConfiguration;
    }

    @Override
    public List<Map<String, Object>> getAllCVs(final String[] fields) {
        if (!es.doesIndexExist(INDEX_CVS)) {
            logger.warn("index " + INDEX_CVS + " does not exist.");
            return new ArrayList<Map<String, Object>>();
        }
        logger.trace("searching for all cvs in es");
        SearchRequestBuilder search = es.client().prepareSearch()
                .setIndices(INDEX_CVS).setTypes(TYPE_CV)
                .setFetchSource(fields, null)
                .setSize(config.getDefaultEsFetchSize());

        SearchResponse response = search.execute().actionGet();
        if (response.getHits().getHits().length < response.getHits().getTotalHits()) {
            search.setSize((int) response.getHits().getTotalHits());
            response = search.execute().actionGet();
        }

        ArrayList<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> hitEntry = new HashMap<String, Object>();
            Map<String, Object> source = hit.getSource();
            for (String key : source.keySet()) {
                if (source.get(key) != null) {
                    hitEntry.put(key, source.get(key));
                } else {
                    hitEntry.put(key, null);
                }
            }
            hitEntry.put("id", hit.getId());
            result.add(hitEntry);
        }
        return result;
    }

    @Override
    public String createCV(final String content) {
        logger.trace("create cv in es: " + content);
        IndexResponse resp = es.client().prepareIndex(INDEX_CVS, TYPE_CV)
                .setSource(content).execute().actionGet();
        return resp.getId();
    }

    @Override
    public String getCVById(final String id) {
        if (!es.doesIndexExist(INDEX_CVS)) {
            logger.warn("index " + INDEX_CVS + " does not exist.");
            return null;
        }
        logger.trace("fetching one cv with id " + id + " in es");
        return es.client().prepareGet(INDEX_CVS, TYPE_CV, id).execute()
                .actionGet().getSourceAsString();
    }
    
    @Override
    public Map<String, Object> getCVMapById(final String id) {
    	if (!es.doesIndexExist(INDEX_CVS)) {
            logger.warn("index " + INDEX_CVS + " does not exist.");
            return null;
        }
        logger.trace("fetching one cv with id " + id + " in es");
        return es.client().prepareGet(INDEX_CVS, TYPE_CV, id).execute()
                .actionGet().getSource();
    }

    @Override
    public void updateCV(final String id, final String content) {
        logger.trace("update cv with id " + id + " in es: " + content);
        es.client().prepareIndex(INDEX_CVS, TYPE_CV, id).setSource(content)
                .execute().actionGet();
    }
    
    @Override
    public void deleteCV(final String id) {
    	if (!es.doesIndexExist(INDEX_CVS)){
    		logger.warn("cv with id: " + id + "does not exist.");
    	}    	
    	logger.trace("delete cv with id: " + id);
    	es.client().prepareDelete(INDEX_CVS,TYPE_CV,id).execute().actionGet();
    }
}
