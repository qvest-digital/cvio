package org.tarent.cvio.server;

import com.yammer.metrics.core.HealthCheck;

/**
 * demo health check.
 * 
 * @author smancke
 * 
 */
public class Health extends HealthCheck {

    /**
     * create.
     */
    public Health() {
        super("demo-test");
    }

    @Override
    protected Result check() {
        return Result.healthy();
    }
}
