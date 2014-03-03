package org.tarent.cvio.server.common;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ES client manager creating a local es node.
 * 
 * @author smancke
 */
public class ESNodeManagerImpl implements ESNodeManager {

    /**
     * the logger.
     */
    private final Logger logger = LoggerFactory
            .getLogger(ESNodeManagerImpl.class);

    /**
     * The elasticsearch Node instance.
     */
    private Node esNode;

    /**
     * the global service configuration.
     */
    private CVIOConfiguration configuration;

    /**
     * Creates a new local node.
     * 
     * @param cfg the global service configuration
     */
    public ESNodeManagerImpl(final CVIOConfiguration cfg) {
        this.configuration = cfg;
    }

    @Override
    public void start() throws Exception {
        logger.info("starting new Elasticsearch cvio node with cluster name 'cvio' and data dir: "
                + configuration.getDataDirectory());
        Settings settings = ImmutableSettings.builder()
                .put("path.data", configuration.getDataDirectory())
                .put("node.name", "local")
                .put("network.host", configuration.getElasticsearchBindHost())
                .put("http.enabled", configuration.isElasticsearchEnableHttp())
                .build();

        esNode = new NodeBuilder()
                .local(true)
                .clusterName("cvio")
                .settings(settings)
                .node();
    }

    @Override
    public void stop() throws Exception {
        logger.info("starting new Elasticsearch cvio node with cluster name 'cvio'");
        esNode.close();
    }

    @Override
    public Client client() {
        return esNode.client();
    }
}
