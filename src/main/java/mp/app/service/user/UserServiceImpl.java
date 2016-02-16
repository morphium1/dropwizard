package mp.app.service.user;

import mp.app.core.User;
import mp.app.dao.user.UserDao;

public class UserServiceImpl implements UserService{

    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(String userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public void addUser(User user) {
        userDao.addUser(user);
    }
}

