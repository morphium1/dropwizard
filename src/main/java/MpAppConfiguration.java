import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import mp.app.config.MongoConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class MpAppConfiguration extends Configuration {

    @JsonProperty
    @Valid
    @NotNull
    private MongoConfiguration mongo;


    public MongoConfiguration getMongo() {
        return mongo;
    }
}
