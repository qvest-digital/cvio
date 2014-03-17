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
     * Create a new CVDBService based on elastic search.
     * 
     * @param esNode the access to elasticsearch
     */
    @Inject
    public CVDBElasticsearch(final ESNodeManager esNode) {
        es = esNode;
    }

    @Override
    public List<Map<String, String>> getAllCVs(final String[] fields) {
        logger.trace("searching for all cvs in es");
        SearchRequestBuilder search = es.client().prepareSearch()
                .setIndices(INDEX_CVS).setTypes(TYPE_CV)
                .setFetchSource(fields, null);

        SearchResponse response = search.execute().actionGet();

        ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, String> hitEntry = new HashMap<String, String>();
            Map<String, Object> source = hit.getSource();
            for (String key : source.keySet()) {
                if (source.get(key) != null) {
                    hitEntry.put(key, source.get(key).toString());
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
        logger.trace("fetching one cv with id " + id + " in es");
        return es.client().prepareGet(INDEX_CVS, TYPE_CV, id).execute()
                .actionGet().getSourceAsString();
    }

    @Override
    public void updateCV(final String id, final String content) {
        logger.trace("update cv with id " + id + " in es: " + content);
        es.client().prepareIndex(INDEX_CVS, TYPE_CV, id).setSource(content)
                .execute().actionGet();
    }
}
