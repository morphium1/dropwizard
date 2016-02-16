package mp.app.health;

import com.codahale.metrics.health.HealthCheck;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.Document;

import java.util.logging.Logger;

public class MongoHealthCheck extends HealthCheck {

    private final Logger logger = Logger.getLogger(getClass().getName());

    public final static String NAME = "MongoHealth";

    MongoClient mongoClient;

    public MongoHealthCheck(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    @Override
    protected Result check() throws Exception {
        try {
            MongoDatabase system = mongoClient.getDatabase("system");
            BsonDocument command = new BsonDocument("dbStats", new BsonInt32(1)).append("scale", new BsonInt32(1));
            Document document = system.runCommand(command);
            logger.info("Mongo health: "+document.toJson());
        }catch(MongoClientException ex) {
            return Result.unhealthy(ex.getMessage());
        }
        return Result.healthy();
    }
}
