package pw.react.backend.reactbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserResource
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserEntity>> retrieveAllUsers()
    {
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    @GetMapping("/users/{login}")
    public ResponseEntity<UserEntity> retrieveUserById(@PathVariable String login)
    {
        return ResponseEntity.ok().body(userRepository.findByLogin(login));
    }
}
