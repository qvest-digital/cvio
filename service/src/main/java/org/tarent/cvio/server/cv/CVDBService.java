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
import org.tarent.cvio.server.CVIOConfiguration;

import com.google.inject.Inject;

public class CVDBService implements CVDB {

	private static final Logger logger = LoggerFactory.getLogger(CVDBService.class);

	private static final String TYPE_CV = "cv";

	private static final String INDEX_CVS = "cvs";	
	
	Client es;
	
	@Inject
	public CVDBService(CVIOConfiguration cfg) {
		es = new TransportClient().addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
	}
	
	@Override
	public List<Map<String,String>> getAllCVs(String[] fields) {

		SearchRequestBuilder search = es.prepareSearch()
				.setIndices(INDEX_CVS)
				.setTypes(TYPE_CV)
				.setFetchSource(fields, null);
				
		SearchResponse response = search
				.execute().actionGet();
		 
		ArrayList<Map<String,String>> result = new ArrayList<Map<String,String>>();
		for (SearchHit hit : response.getHits().getHits() ) {
			Map<String,String> hitEntry = new HashMap<String,String>();
			Map<String,Object> source = hit.getSource();
			for (String key : source.keySet())
				hitEntry.put(key, source.get(key) == null ? null : source.get(key).toString());
			hitEntry.put("id", hit.getId());
			result.add(hitEntry);
		}
		return result;
	}

	@Override
	public String createCV(String content) {
		IndexResponse resp = es.prepareIndex(INDEX_CVS, TYPE_CV)
				.setSource(content)
				.execute()
				.actionGet();
		return resp.getId();
	}

	@Override
	public String getCVById(String id) {
		return es.prepareGet(INDEX_CVS, TYPE_CV, id)
				.execute()
				.actionGet()
				.getSourceAsString();
	}

	@Override
	public void updateCV(String id, String content) {
		es.prepareIndex(INDEX_CVS, TYPE_CV, id)
				.setSource(content)
				.execute()
				.actionGet();
	}
}
