package org.tarent.cvio.server;

import java.io.File;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.tarent.cvio.server.common.CVIOConfiguration;
import org.tarent.cvio.server.common.ESNodeManager;
import org.tarent.cvio.server.common.ESNodeManagerImpl;

/**
 * Base class for elasticsearch based tests, supplies a clean es database.
 * 
 * @author smancke
 */
public class EsTest {

    private ESNodeManagerImpl esNode;
    private File dataDir = new File(System.getProperty("java.io.tmpdir"),
            "cvio-unittest-dir-" + UUID.randomUUID().toString());

    public EsTest() {
        super();
    }

    @Before
    public void setUpEs() throws Exception {

        esNode = new ESNodeManagerImpl(new CVIOConfiguration() {
            @Override
            public String getDataDirectory() {
                if (!dataDir.exists())
                    dataDir.mkdirs();
                return dataDir.getAbsolutePath();
            }

            @Override
            public boolean isElasticsearchEnableHttp() {
                return false;
            }

            @Override
            public String getElasticsearchBindHost() {
                return "127.0.0.1";
            }
        });

        esNode.start();
    }

    @After
    public void tearDownEs() throws Exception {
        esNode.stop();
        FileUtils.deleteDirectory(dataDir);
    }

    protected void refreshIndexes() {
        esNode().client().admin().indices().prepareRefresh().execute().actionGet();
    }

    protected ESNodeManager esNode() {
        return esNode;
    }
}