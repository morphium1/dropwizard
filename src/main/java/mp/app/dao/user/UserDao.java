package mp.app.dao.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import mp.app.core.User;

public class UserDao extends Dao<User>{

    public UserDao(MongoClient mongoClient, ObjectMapper objectMapper) {
        super(mongoClient,objectMapper,User.class);
    }

    public User getUserById(String userId) {
        return collection.findOneById(userId);
    }

    public void addUser(User user) {
        collection.insert(user);
    }
}
