package databases.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.posts.utils.Encryption;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.LOWER_CASE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "userid")
    private String userid;

    @Column(name = "password")
    private String password;

    public User() {}

    @JsonCreator
    public User(@JsonProperty("username") String username,
                @JsonProperty("userid") String userid,
                @JsonProperty("password") String password) {
        setUsername(username);
        setUserid(userid);
        setPassword(password, userid);
    }

    public User(String userid, String password) {
        setUserid(userid);
        setPassword(password, userid);
    }

    public boolean isValid() {
        return !username.isEmpty() && !userid.isEmpty() && !password.isEmpty();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    private void setUserid(String userid) {
        this.userid = userid;
    }

    private void setPassword(String password, String userid) {
        this.password = Encryption.getSaltedKey(password, userid);
    }
}
