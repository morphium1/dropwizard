import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import mp.app.config.MongoClientFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MpAppConfiguration extends Configuration {

//    @JsonProperty
//    @Valid
//    @NotNull
//    private MongoConfiguration mongo;
//
//
//    public MongoConfiguration getMongo() {
//        return mongo;
//    }

    @Valid
    @NotNull
    @JsonProperty("mongo")
    private MongoClientFactory mongoClientFactory = new MongoClientFactory();


    public MongoClientFactory getMongoClientFactory() {
        return mongoClientFactory;
    }


}
