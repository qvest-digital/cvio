
package cvserver;

import com.yammer.metrics.core.HealthCheck;

public class Health extends HealthCheck {

    public Health() {
        super("test");
    }

    @Override
    protected Result check() throws Exception {
        return Result.unhealthy("mit geht et nisch jut ....");
        //return Result.healthy();
    }
}