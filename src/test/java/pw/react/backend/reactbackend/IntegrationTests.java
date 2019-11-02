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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

    @Autowired
    private TestRestTemplate restTemplate = new TestRestTemplate();
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

    private HttpHeaders headers = createAndInitializeHeader();

    private HttpHeaders createAndInitializeHeader()
    {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "secretCode");
        return headers;
    }

    @Autowired
    private UserRepository userRepository;


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Before
    public void setup()
    {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());

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

        UserEntity foundUser = userRepository.findByLogin("jaku");

        ResponseEntity<String> userById1 = restTemplate.exchange(
                createURLWithPort("/users/" + foundUser.getId().toString()),
                HttpMethod.GET,
                httpEntity,
                String.class);

        ResponseEntity<String> userById2 = restTemplate.exchange(
                createURLWithPort("/users/19"),
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
        UserEntity foundUser = userRepository.findByLogin("jaku");
        UserEntity updatedUser = new UserEntity(foundUser.getId(), "Wladyslaw", "Kurczynski", "wlaku", LocalDate.parse("2000-11-11"),false);

        HttpEntity<UserEntity> httpEntity = new HttpEntity<UserEntity>(updatedUser,headers);

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

    @Test
    public void testUpdateUserPartial() throws JSONException
    {
        UserEntity foundUser = userRepository.findByLogin("kawi");

        UserEntity updatedUser = new UserEntity(foundUser.getId(), null, "Niewielki", null, null,false);

        HttpEntity<UserEntity> httpEntity = new HttpEntity<UserEntity>(updatedUser,headers);

        ResponseEntity<String> updatedUserResponse = restTemplate.exchange(
                createURLWithPort("/users"),
                HttpMethod.PATCH,
                httpEntity,
                String.class);

        String response =
                "{" +
                "\"date_of_birth\": \"2001-05-14\"," +
                "\"login\": \"kawi\"," +
                "\"active\": false," +
                "\"first_name\": \"Kazimierz\"," +
                "\"last_name\": \"Niewielki\"" +
                "}";

        JSONAssert.assertEquals(response, updatedUserResponse.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testCreateUser() throws JSONException
    {
        UserEntity newUser = new UserEntity("Antoni", "Banderaz", "anba", LocalDate.parse("1996-01-11"),true);

        HttpEntity<UserEntity> httpEntity = new HttpEntity<UserEntity>(newUser,headers);

        ResponseEntity<String> updatedUserResponse = restTemplate.exchange(
                createURLWithPort("/users"),
                HttpMethod.POST,
                httpEntity,
                String.class);

        String response =
                "{" +
                        "\"date_of_birth\": \"1996-01-11\"," +
                        "\"login\": \"anba\"," +
                        "\"active\": true," +
                        "\"first_name\": \"Antoni\"," +
                        "\"last_name\": \"Banderaz\"" +
                        "}";

        JSONAssert.assertEquals(response, updatedUserResponse.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testDeleteUser()
    {
        HttpEntity httpEntity = new HttpEntity(headers);

        ResponseEntity<String> deleteUserResponse1 = restTemplate.exchange(
                createURLWithPort("/users/kawi"),
                HttpMethod.DELETE,
                httpEntity,
                String.class);

        ResponseEntity<String> deleteUserResponse2 = restTemplate.exchange(
                createURLWithPort("/users/abc"),
                HttpMethod.DELETE,
                httpEntity,
                String.class);

        String response1 = "User [kawi] has been deleted";
        String response2 = "User with login abc does not exist";

        assertThat(response1.compareTo(deleteUserResponse1.getBody()) == 0);
        assertThat(response2.compareTo(deleteUserResponse2.getBody()) == 0);
    }



}
