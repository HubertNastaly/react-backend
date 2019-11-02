package pw.react.backend.reactbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pw.react.backend.reactbackend.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    public UserEntity updateUser(UserEntity user)
    {
        Optional<UserEntity> foundUser = userRepository.findById(user.getId());
        if(foundUser.isPresent())
        {
            if(user.getFirstName() == null)
            {
                user.setFirstName(foundUser.get().getFirstName());
            }
            if(user.getLastName() == null)
            {
                user.setLastName(foundUser.get().getLastName());
            }
            if(user.getLogin() == null)
            {
                user.setLogin(foundUser.get().getLogin());
            }
            if(user.getDateOfBirth() == null)
            {
                user.setDateOfBirth(foundUser.get().getDateOfBirth());
            }
            return userRepository.save(user);
        }
        throw new ResourceNotFoundException("User with id: " + user.getId() + " does not exist");
    }

    public boolean deleteUserByLogin(String login)
    {
        if(isUserCreated(login))
        {
            userRepository.deleteByLogin(login);
            return true;
        }
        return false;
    }

    public Optional<UserEntity> findUserById(Long id)
    {
        return userRepository.findById(id);
    }
}
