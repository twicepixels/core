package twice.domain.repository;

import twice.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    User add(String name);
    Optional<User> findByLogin(String login);
}
