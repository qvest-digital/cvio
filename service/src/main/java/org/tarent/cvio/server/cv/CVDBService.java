package org.tarent.cvio.server.cv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * elasticsearch based implementation of the CVDB.
 * 
 * @author smancke
 */
public class CVDBService implements CVDB {

	private final Logger logger = LoggerFactory.getLogger(CVDBService.class);

	private static final String TYPE_CV = "cv";

	private static final String INDEX_CVS = "cvs";	
	
	private Client es;
	
	@Inject
	public CVDBService() {
		es = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
	}
	
	@Override
	public List<Map<String, String>> getAllCVs(String[] fields) {
		logger.trace("searching for all cvs in es");
		SearchRequestBuilder search = es.prepareSearch()
				.setIndices(INDEX_CVS)
				.setTypes(TYPE_CV)
				.setFetchSource(fields, null);
				
		SearchResponse response = search
				.execute().actionGet();
		 
		ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();
		for (SearchHit hit : response.getHits().getHits()) {
			Map<String, String> hitEntry = new HashMap<String, String>();
			Map<String, Object> source = hit.getSource();
			for (String key : source.keySet()) {
				hitEntry.put(key, source.get(key) == null ? null : source.get(key).toString());
			}
			hitEntry.put("id", hit.getId());
			result.add(hitEntry);
		}
		return result;
	}

	@Override
	public String createCV(final String content) {
		logger.trace("create cv in es");
		IndexResponse resp = es.prepareIndex(INDEX_CVS, TYPE_CV)
				.setSource(content)
				.execute()
				.actionGet();
		return resp.getId();
	}

	@Override
	public String getCVById(final String id) {
		logger.trace("fetching one cv with id " + id +" in es");
		return es.prepareGet(INDEX_CVS, TYPE_CV, id)
				.execute()
				.actionGet()
				.getSourceAsString();
	}

	@Override
	public void updateCV(final String id, final String content) {
		logger.trace("update cv with id " + id +" in es");
		es.prepareIndex(INDEX_CVS, TYPE_CV, id)
				.setSource(content)
				.execute()
				.actionGet();
	}
}
