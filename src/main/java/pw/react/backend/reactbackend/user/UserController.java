package pw.react.backend.reactbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pw.react.backend.reactbackend.exceptions.ResourceNotFoundException;

import javax.transaction.Transactional;
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

    @GetMapping(path = "/by-login/{login}")
    public ResponseEntity<String> retrieveUserByLogin(@PathVariable String login)
    {
        if(userService.isUserCreated(login))
        {
            return ResponseEntity.ok().body("User with login: " + login + " exists in database");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserEntity> retrieveUserById(@PathVariable Long id)
    {
        Optional<UserEntity> user = userService.findUserById(id);
        if(user.isPresent())
        {
            return ResponseEntity.ok().body(user.get());
        }
        throw new ResourceNotFoundException("User with id " + id.toString() + "does not exist");
    }

    @PutMapping(path = "")
    public ResponseEntity<UserEntity> updateUser(@RequestBody @Valid UserEntity user)
    {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }

    @PatchMapping(path="")
    public ResponseEntity<UserEntity> updateUserPartial(@RequestBody @Valid UserEntity user)
    {
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @PostMapping(path = "")
    public ResponseEntity<UserEntity> createUser(@RequestBody @Valid UserEntity user)
    {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }

    @DeleteMapping(path = "/{login}")
    @Transactional
    public ResponseEntity<String> deleteUser(@PathVariable String login)
    {
        if(userService.deleteUserByLogin(login))
        {
            return ResponseEntity.ok().body(String.format("User [%s] has been deleted", login));
        }
        throw new ResourceNotFoundException("User with login " + login + " does not exist");
    }
}
