package twice.repository.impl;

import org.springframework.stereotype.Component;
import twice.domain.repository.UserDao;
import twice.domain.TwiceUser;

import java.util.UUID;

@Component
public class UserDaoImpl implements UserDao {

    @Override
    public TwiceUser add(String name) {
        return new TwiceUser() {{
            setId(UUID.randomUUID());
            setName(String.format("Hello, %s!", name));
        }};
    }
}
