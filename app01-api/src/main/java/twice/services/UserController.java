package twice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import twice.domain.dto.UserDTO;
import twice.domain.model.User;
import twice.domain.service.UserService;
import twice.security.SecurityUtils;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<UserDTO> user() {
        String login = SecurityUtils.getCurrentUser().getUsername();
        return Optional.ofNullable(userService.findByLogin(login))
                .map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @CrossOrigin
    @RequestMapping("/add")
    public User addUser(@RequestParam(value = "name",
            defaultValue = "World") final String name) {
        //invocar al service de usuarios
        return userService.add(name);
    }
}