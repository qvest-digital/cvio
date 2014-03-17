package org.tarent.cvio.server.skill;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tarent.cvio.server.common.ESNodeManager;
import org.tarent.cvio.server.common.SilentObjectMapper;

import com.google.inject.Inject;

/**
 * Elasticsearch based implementation of the SkillDB.
 * 
 * @author smancke
 */
public class SkillDBElasticsearch implements SkillDB {

    /**
     * Mapper for json/pojo handling.
     */
    private static SilentObjectMapper mapper = new SilentObjectMapper();

    /**
     * the logger.
     */
    private final Logger logger = LoggerFactory
            .getLogger(SkillDBElasticsearch.class);

    /**
     * The eslasticsearch type for skill entries.
     */
    private static final String TYPE_SKILL = "skill";

    /**
     * The elasticsearch index name for skills.
     */
    private static final String INDEX_SKILL = "skills";

    /**
     * The elasticsearch client manager instance.
     */
    private ESNodeManager es;

    /**
     * Create a new SkillDBService based on elasticsearch.
     * 
     * @param esNode the access to elasticsearch
     */
    @Inject
    public SkillDBElasticsearch(final ESNodeManager esNode) {
        es = esNode;
    }

    @Override
    public List<Skill> getAllSkills() {
        logger.trace("searching for all skills es");
        SearchRequestBuilder search = es.client().prepareSearch()
                .setIndices(INDEX_SKILL).setTypes(TYPE_SKILL);

        SearchResponse response = search.execute().actionGet();

        return skillListFromResponse(response);
    }

    /**
     * Returns an List<Skill> for a search response.
     * 
     * @param response the response of a search
     * @return the list
     */
    private List<Skill> skillListFromResponse(final SearchResponse response) {
        ArrayList<Skill> result = new ArrayList<Skill>();
        for (SearchHit hit : response.getHits().getHits()) {
            String sourceString = hit.getSourceAsString();

            Skill skill = mapper.readValue(sourceString, Skill.class);
            skill.setId(hit.getId());
            result.add(skill);
        }
        return result;
    }

    @Override
    public Skill getSkillById(final String id) {
        logger.trace("fetching one skill with name " + id + " in es");
        String sourceString = es.client()
                .prepareGet(INDEX_SKILL, TYPE_SKILL, id).execute()
                .actionGet().getSourceAsString();
        return mapper.readValue(sourceString, Skill.class);
    }

    @Override
    public String createSkill(final Skill newSkill) {
        logger.trace("create a skill in es: " + newSkill.getName());
        IndexResponse resp = es.client().prepareIndex(INDEX_SKILL, TYPE_SKILL)
                .setSource(mapper.writeValueAsString(newSkill)).execute()
                .actionGet();
        return resp.getId();
    }
}
