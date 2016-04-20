package twice.repository.impl;

import org.springframework.stereotype.Component;
import twice.domain.model.Authority;
import twice.domain.model.User;
import twice.domain.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository {

    private List<User> list = new ArrayList<>();
    private List<Authority> authorities = new ArrayList<>();

    public UserRepositoryImpl() {
        authorities.add(new Authority() {{
            setName("ROLE_USER");
        }});
        authorities.add(new Authority() {{
            setName("ROLE_ADMIN");
        }});

        list.add(new User() {{
            setId(5);
            setName("diego");
            setActivated(true);
            setPassword("abcde");
            setAuthorities(authorities);
        }});
        list.add(new User() {{
            setId(8);
            setName("juanjo");
            setActivated(true);
            setPassword("abcde");
            setAuthorities(authorities);
        }});
        list.add(new User() {{
            setId(3);
            setName("gabriel");
            setActivated(true);
            setPassword("abcde");
            setAuthorities(authorities);
        }});
        list.add(new User() {{
            setId(4);
            setName("eduardo");
            setActivated(true);
            setPassword("abcde");
            setAuthorities(authorities);
        }});
    }

    @Override
    public User add(String name) {
        return new User() {{
            setId(1);
            setName(String.format("Hello, %s!", name));
        }};
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return list.stream()
                .filter(x -> Objects.equals(x.getName(), login))
                .findFirst();
    }
}
