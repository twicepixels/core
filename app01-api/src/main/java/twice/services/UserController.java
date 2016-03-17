package twice.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twice.domain.TwiceUser;

import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/add")
    public TwiceUser addUser(@RequestParam(value = "name",
            defaultValue = "World") final String name) {

        return new TwiceUser() {{
            setId(UUID.randomUUID());
            setName(String.format("Hello, %s!", name));
        }};
    }
}