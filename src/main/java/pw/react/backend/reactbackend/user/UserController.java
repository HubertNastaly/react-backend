package pw.react.backend.reactbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.reactbackend.exceptions.ResourceNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController
{
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserEntity>> retrieveAllUsers()
    {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping("/by-login/{login}")
    public ResponseEntity<String> retrieveUserByLogin(@PathVariable String login)
    {
        if(userService.isUserCreated(login))
        {
            return ResponseEntity.ok().body("User with login: " + login + " exists in database");
        }
        else
        {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> retrieveUserById(@PathVariable Long id)
    {
        Optional<UserEntity> user = userService.findUserById(id);
        if(user.isPresent())
        {
            return ResponseEntity.ok().body(user.get());
        }
        throw new ResourceNotFoundException("User with id " + id.toString() + "does not exist");
    }

    @PostMapping(path = "")
    public ResponseEntity<UserEntity> createUser(@RequestBody @Valid UserEntity user)
    {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }
}
