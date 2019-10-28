import javax.persistence.*;

@Entity
@Table(name="Users")
public class User
{
    @Id
    @GeneratedValue
    private int id;

    @Column(name="First name")
    private String FirstName;

    @Column(name="Last name")
    private String LastName;

    @Column(name="Login")
    private String Login;

    @Column(name="Date of birth")
    private String DateOfBirth;

    @Column(name="Active")
    private boolean Active;

    public User(String FirstName, String LastName, String Login, String DateOfBirth, boolean Active)
    {
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.Login = Login;
        this.DateOfBirth = DateOfBirth;
        this.Active = Active;
    }

    public int getId()
    {
        return this.id;
    }

    public String getFirstName()
    {
        return this.FirstName;
    }

    public String getLastName()
    {
        return this.LastName;
    }

    public String getLogin()
    {
        return this.Login;
    }

    public String getDateOfBirth()
    {
        return this.DateOfBirth;
    }

    public boolean getActive()
    {
        return this.Active;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setFirstName(String firstName)
    {
        this.FirstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.LastName = lastName;
    }

    public void setLogin(String login)
    {
        this.Login = login;
    }

    public void setDateOfBirth(String dateOfBirth)
    {
        this.DateOfBirth = dateOfBirth;
    }

    public void setActive(boolean active)
    {
        this.Active = active;
    }
}
