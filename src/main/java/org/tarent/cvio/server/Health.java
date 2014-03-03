
package org.tarent.cvio.server;

import com.yammer.metrics.core.HealthCheck;

public class Health extends HealthCheck {

    public Health() {
        super("demo-test");
    }

    @Override
    protected Result check() {
        return Result.healthy();
    }
}