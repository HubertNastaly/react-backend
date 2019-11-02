package pw.react.backend.reactbackend;

import org.junit.Before;
import org.junit.Test;
import pw.react.backend.reactbackend.user.UserEntity;
import pw.react.backend.reactbackend.user.UserRepository;
import pw.react.backend.reactbackend.user.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UnitTests
{
    private UserRepository userRepository;
    private UserService userService;

    @Before
    public  void setup()
    {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    public void TestGetAllUsers() {
        List<UserEntity> expectedUsers = new ArrayList<UserEntity>();
        expectedUsers.add(new UserEntity("Tomasz", "Niestaly", "tonie", LocalDate.parse("2001-09-09"), true));
        expectedUsers.add(new UserEntity("Jan", "Kowalski", "jako", LocalDate.parse("2002-10-19"), true));
        expectedUsers.add(new UserEntity("Zbigniew", "Krzeslo", "zbik", LocalDate.parse("1998-03-04"), false));
        when(userRepository.findAll()).thenReturn(expectedUsers);

        List<UserEntity> actualUsers = userService.getAllUsers();

        if(expectedUsers.size() != actualUsers.size())
        {
            fail("unequal sizes");
        }
        boolean equal = true;
        for(int i=0; i<expectedUsers.size(); i++)
        {
            equal = (expectedUsers.get(i).getId() == actualUsers.get(i).getId());
            if(!equal)
            {
                fail("different field values");
            }
            equal = (expectedUsers.get(i).getFirstName() == actualUsers.get(i).getFirstName());
            if(!equal)
            {
                fail("different field first name");
            }
            equal = (expectedUsers.get(i).getLastName() == actualUsers.get(i).getLastName());
            if(!equal)
            {
                fail("different field last name");
            }
            equal = (expectedUsers.get(i).getDateOfBirth() == actualUsers.get(i).getDateOfBirth());
            if(!equal)
            {
                fail("different field date");
            }
            equal = (expectedUsers.get(i).getLogin() == actualUsers.get(i).getLogin());
            if(!equal)
            {
                fail("different field login");
            }
            equal = (expectedUsers.get(i).getActive() == actualUsers.get(i).getActive());
            if(!equal)
            {
                fail("different field active");
            }
        }
    }

    @Test
    public void TestIsUserCreated()
    {
        when(userRepository.findByLogin("tonie")).thenReturn(new UserEntity("Tomasz", "Niestaly", "tonie", LocalDate.parse("2001-09-09"), true));
        when(userRepository.findByLogin("nieto")).thenReturn(null);
        assertThat(userService.isUserCreated("tonie") == true);
        assertThat(userService.isUserCreated("nieto") == false);
    }

    @Test
    public void TestDeleteUserByLogin()
    {
        when(userRepository.deleteByLogin("nieto")).thenReturn((long) 0);
        when(userRepository.deleteByLogin("tonie")).thenReturn((long) 1);

        assertThat(userService.deleteUserByLogin("nieto") == false);
        assertThat(userService.deleteUserByLogin("tonie") == true);
    }

    @Test
    public void TestUpdateUser()
    {
        UserEntity testedUser = new UserEntity("Tomasz", "Niestaly", "tonie", LocalDate.parse("2001-09-09"), true);
        UserEntity newUser1 = new UserEntity((long)1,"Jan", "Niestaly", "tonie", LocalDate.parse("2001-09-09"), true);

        when(userRepository.findById((long)1)).thenReturn(Optional.of(testedUser));
        when(userRepository.save(newUser1)).thenReturn(newUser1);


        assertThat(userService.updateUser(newUser1) == newUser1);
    }

    @Test
    public void TestSaveUser()
    {
        UserEntity userToSave = new UserEntity("Tomasz", "Niestaly", "tonie", LocalDate.parse("2001-09-09"), true);
        when(userRepository.save(userToSave)).thenReturn(userToSave);
        assertThat(userService.saveUser(userToSave) == userToSave);
    }
}
