package mp.app.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;

import javax.validation.constraints.NotNull;

public class MongoClientFactory {

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

    public MongoClient build() {
        MongoClientOptions mongoClientOptions = MongoClientOptions.builder().serverSelectionTimeout(0).build();
        ServerAddress serverAddress = new ServerAddress(getHost(),getPort());
        MongoClient mongoClient = new MongoClient(serverAddress,mongoClientOptions);
        return mongoClient;
    }
}
