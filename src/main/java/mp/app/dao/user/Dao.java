package mp.app.dao.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.mongojack.JacksonDBCollection;
import org.mongojack.internal.MongoJackModule;

public class Dao<T> {

    protected final JacksonDBCollection<T, String> collection;

    public  Dao(MongoClient mongoClient, ObjectMapper objectMapper, Class clazz) {
        DB db = mongoClient.getDB("dropwizard");
        DBCollection dbCollection = db.getCollection(clazz.getSimpleName().toLowerCase());
        MongoJackModule.configure(objectMapper);
        collection = JacksonDBCollection.wrap(dbCollection, clazz, String.class, objectMapper);
    }
}
