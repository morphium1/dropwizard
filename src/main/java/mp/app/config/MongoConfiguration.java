package mp.app.config;

import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

public class MongoConfiguration extends Configuration {
    @NotNull
    private String host;
    @NotNull
    private int port;

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

}
