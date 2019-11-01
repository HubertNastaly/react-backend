package pw.react.backend.reactbackend.user;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import pw.react.backend.reactbackend.utils.JsonDateDeserializer;
import pw.react.backend.reactbackend.utils.JsonDateSerializer;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="users")
public class UserEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="first_name")
    private String first_name;

    @Column(name="last_name")
    private String last_name;

    @Column(name="login")
    private String login;

    @Column(name="date_of_birth")
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private LocalDate date_of_birth;

    @Column(name="active")
    private boolean active;

    public UserEntity(){}

    public UserEntity(String FirstName, String LastName, String Login, LocalDate DateOfBirth, boolean Active)
    {
        this.first_name = FirstName;
        this.last_name = LastName;
        this.login = Login;
        this.date_of_birth = DateOfBirth;
        this.active = Active;
    }

    @Override
    public String toString() {
        return "pw.react.backend.reactbackend.usereact.backend.reactbackend.UserEntity [id=" + id + ", firstName=" + first_name +
                ", lastName=" + last_name + ", login=" + login +
                ", dateOfBirth=" + date_of_birth + ", active=" + active +"]";
    }

    @JsonGetter("id")
    public Long getId()
    {
        return this.id;
    }

    @JsonGetter("first_name")
    public String getFirstName()
    {
        return this.first_name;
    }

    @JsonGetter("last_name")
    public String getLastName()
    {
        return this.last_name;
    }

    @JsonGetter("login")
    public String getLogin()
    {
        return this.login;
    }

    @JsonGetter("date_of_birth")
    public LocalDate getDateOfBirth()
    {
        return this.date_of_birth;
    }

    @JsonGetter("active")
    public boolean getActive()
    {
        return this.active;
    }

    @JsonSetter("id")
    public void setId(Long id)
    {
        this.id = id;
    }

    @JsonSetter("first_name")
    public void setFirstName(String firstName)
    {
        this.first_name = firstName;
    }

    @JsonSetter("last_name")
    public void setLastName(String lastName)
    {
        this.last_name = lastName;
    }

    @JsonSetter("login")
    public void setLogin(String login)
    {
        this.login = login;
    }

    @JsonSetter("date_of_birth")
    public void setDateOfBirth(LocalDate dateOfBirth)
    {
        this.date_of_birth = dateOfBirth;
    }

    @JsonSetter("active")
    public void setActive(boolean active)
    {
        this.active = active;
    }
}
