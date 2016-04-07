package twice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import twice.domain.TwiceUser;
import twice.domain.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public TwiceUser addUser(@RequestParam(value = "name",
            defaultValue = "World") final String name) {
        //invocar al service de usuarios
        return userService.add(name);
    }
}