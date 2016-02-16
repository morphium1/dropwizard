package mp.app.service.user;

import mp.app.core.User;

public interface UserService {
    User getUserById(String userId);
    void addUser(User user);
}
