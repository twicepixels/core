package twice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import twice.domain.repository.UserDao;
import twice.domain.TwiceUser;
import twice.domain.service.UserService;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public TwiceUser add(String name) {
        return userDao.add(name);
    }
}
