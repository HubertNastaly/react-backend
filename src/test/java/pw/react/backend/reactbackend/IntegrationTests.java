package pw.react.backend.reactbackend;

import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import pw.react.backend.reactbackend.user.UserEntity;
import pw.react.backend.reactbackend.user.UserRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTests
{
    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Autowired
    private UserRepository userRepository;


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Before
    public void insertIntoDb()
    {

        UserEntity user1 = new UserEntity("Jaroslaw", "Kuczynski", "jaku", LocalDate.parse("2000-12-12"), true);
        UserEntity user2 = new UserEntity("Kazimierz", "Wielki", "kawi", LocalDate.parse("2001-05-14"), true);
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @After
    public void deleteDb()
    {
        userRepository.deleteAll();
    }

    @Test
    public void testRetrieveAllUsers() throws JSONException {
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> allUsers = restTemplate.exchange(
                createURLWithPort("/users"),
                HttpMethod.GET,
                httpEntity,
                //new ParameterizedTypeReference<List<UserEntity>>() {});
                String.class);

        String response = "[" +
        "{" +
            "\"date_of_birth\": \"2000-12-12\"," +
                "\"login\": \"jaku\"," +
                "\"active\": true," +
                "\"first_name\": \"Jaroslaw\"," +
                "\"last_name\": \"Kuczynski\"" +
        "}," +
        "{" +
            "\"date_of_birth\": \"2001-05-14\"," +
            "\"login\": \"kawi\"," +
            "\"active\": true," +
            "\"first_name\": \"Kazimierz\"," +
            "\"last_name\": \"Wielki\"" +
        "}" +
        "]";

        JSONAssert.assertEquals(response, allUsers.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testRetrieveUserByLogin() {
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> userByLoginMsg1 = restTemplate.exchange(
                createURLWithPort("/users/by-login/jaku"),
                HttpMethod.GET,
                httpEntity,
                String.class);

        ResponseEntity<String> userByLoginMsg2 = restTemplate.exchange(
                createURLWithPort("/users/by-login/gallanonim"),
                HttpMethod.GET,
                httpEntity,
                String.class);

        String response1 = "User with login: jaku exists in database";

        assertThat(response1.compareTo(userByLoginMsg1.getBody()) == 0);
        assertThat(userByLoginMsg2.getBody() == null);
    }

    @Test
    public void testRetrieveUserById() throws JSONException {
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> userById1 = restTemplate.exchange(
                createURLWithPort("/users/1"),
                HttpMethod.GET,
                httpEntity,
                String.class);

        ResponseEntity<String> userById2 = restTemplate.exchange(
                createURLWithPort("/users/9"),
                HttpMethod.GET,
                httpEntity,
                String.class);


        String response =
                "{" +
                "\"date_of_birth\": \"2000-12-12\"," +
                "\"login\": \"jaku\"," +
                "\"active\": true," +
                "\"first_name\": \"Jaroslaw\"," +
                "\"last_name\": \"Kuczynski\"" +
                "}";

        JSONAssert.assertEquals(response, userById1.getBody(), JSONCompareMode.LENIENT);
        assertThat(userById2.getStatusCode() == HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateUser() throws JSONException {

        UserEntity updatedUser = new UserEntity((long)1, "Wladyslaw", "Kurczynski", "wlaku", LocalDate.parse("2000-11-11"),false);

        HttpEntity<UserEntity> httpEntity = new HttpEntity(updatedUser);

        ResponseEntity<String> updatedUserResponse = restTemplate.exchange(
                createURLWithPort("/users"),
                HttpMethod.PUT,
                httpEntity,
                String.class);

        String response =
                "{" +
                "\"date_of_birth\": \"2000-11-11\"," +
                "\"login\": \"wlaku\"," +
                "\"active\": false," +
                "\"first_name\": \"Wladyslaw\"," +
                "\"last_name\": \"Kurczynski\"" +
                "}";

        JSONAssert.assertEquals(response, updatedUserResponse.getBody(), JSONCompareMode.LENIENT);
    }

}
