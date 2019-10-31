package pw.react.backend.reactbackend.user;

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
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="login")
    private String login;

    @Column(name="date_of_birth")
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private LocalDate dateOfBirth;

    @Column(name="active")
    private boolean active;

    public UserEntity(){}

    public UserEntity(String FirstName, String LastName, String Login, LocalDate DateOfBirth, boolean Active)
    {
        this.firstName = FirstName;
        this.lastName = LastName;
        this.login = Login;
        this.dateOfBirth = DateOfBirth;
        this.active = Active;
    }

    @Override
    public String toString() {
        return "pw.react.backend.reactbackend.usereact.backend.reactbackend.UserEntity [id=" + id + ", firstName=" + firstName +
                ", lastName=" + lastName + ", login=" + login +
                ", dateOfBirth=" + dateOfBirth + ", active=" + active +"]";
    }

    public Long getId()
    {
        return this.id;
    }

    public String getFirstName()
    {
        return this.firstName;
    }

    public String getLastName()
    {
        return this.lastName;
    }

    public String getLogin()
    {
        return this.login;
    }

    public LocalDate getDateOfBirth()
    {
        return this.dateOfBirth;
    }

    public boolean getActive()
    {
        return this.active;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setLogin(String login)
    {
        this.login = login;
    }

    public void setDateOfBirth(LocalDate dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
}
