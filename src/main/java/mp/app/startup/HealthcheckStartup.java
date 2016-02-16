package mp.app.startup;

import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import io.dropwizard.lifecycle.Managed;

import java.util.SortedMap;

public class HealthcheckStartup implements Managed {

    private final HealthCheckRegistry healthCheckRegistry;

    public HealthcheckStartup(HealthCheckRegistry healthCheckRegistry) {
        this.healthCheckRegistry = healthCheckRegistry;
    }

    @Override
    public void start() throws Exception {
        SortedMap<String, HealthCheck.Result> results = healthCheckRegistry.runHealthChecks();
        StringBuffer sb = new StringBuffer();
        results.values().stream().filter(r -> !r.isHealthy()).forEach(r -> sb.append(r.getMessage()+"\n"));
        if (sb.length() > 0) {
            throw new RuntimeException(sb.toString());
        }
    }

    @Override
    public void stop() throws Exception {

    }
}
