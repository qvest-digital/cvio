package org.tarent.cvio.server.cv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.tarent.cvio.server.ConfigurationHelper;
import org.tarent.cvio.server.EsTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CVDBElasticsearchTest extends EsTest {

    /**
     * the jackson mapper.
     */
    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testList() throws JsonProcessingException {
        // given a fresh database
        CVDBElasticsearch cvDB = new CVDBElasticsearch(esNode(), ConfigurationHelper.cfg());

        // when I create a Skill
        Map<String, Object> newCV = CVHelper.demoCVWithoutId();
        String id = cvDB.createCV(mapper.writeValueAsString(newCV));
        for (int i = 0; i < 20; i++) {
            cvDB.createCV(mapper.writeValueAsString(CVHelper.demoCVWithoutId()));
        }

        refreshIndexes();

        // then I can retrieve it by its name later
        List<Map<String, Object>> retrievedCVs = cvDB.getAllCVs(null);
        assertEquals(21, retrievedCVs.size());
    }

    @Test
    public void testNullValuesOnNonExistingIndexex() {
        // given a fresh database
        CVDBElasticsearch cvDB = new CVDBElasticsearch(esNode(), ConfigurationHelper.cfg());

        assertEquals(0, cvDB.getAllCVs(null).size());
        assertNull(cvDB.getCVById("foo"));
    }
}
