package pw.react.backend.reactbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    @Autowired
    UserRepository userRepository;

    public List<UserEntity> getAllUsers()
    {
        List<UserEntity> usersList = userRepository.findAll();
        if(usersList.size() > 0)
        {
            return usersList;
        }
        return new ArrayList<UserEntity>();
    }

    public boolean isUserCreated(String login)
    {
        if (userRepository.findByLogin(login) != null)
        {
            return true;
        }
        return false;
    }

    public UserEntity saveUser(UserEntity user)
    {
        return userRepository.save(user);
    }

    public Optional<UserEntity> findUserById(Long id)
    {
        return userRepository.findById(id);
    }
}
