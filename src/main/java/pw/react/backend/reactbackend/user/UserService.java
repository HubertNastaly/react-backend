package pw.react.backend.reactbackend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

//    public void ifUserCreated(String login)
//    {
//        UserEntity usersWithLogin = userRepository.findByLogin(login);
//    }
}
