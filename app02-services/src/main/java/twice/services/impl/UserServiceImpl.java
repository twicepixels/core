package twice.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import twice.domain.model.User;
import twice.domain.repository.UserRepository;
import twice.domain.service.UserService;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User add(String name) {
        return userRepository.add(name);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByLogin(String login) {
        return userRepository.findByLogin(login).get();
    }
}
