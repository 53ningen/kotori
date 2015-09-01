package databases.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.posts.utils.Encryption;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.LOWER_CASE)
public class User {

    @Id
    @Column(name = "userid")
    private String userid;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    public User() {}

    @JsonCreator
    public User(@JsonProperty("userid") String userid,
                @JsonProperty("username") String username,
                @JsonProperty("password") String password) {
        setUserid(userid);
        setUsername(username);
        setPassword(password, userid);
    }

    public boolean isValid() {
        return !username.isEmpty() && !userid.isEmpty() && !password.isEmpty();
    }

    public boolean isValidLogin() {
        return !userid.isEmpty() && !password.isEmpty();
    }

    public String getUserid() {
        return userid;
    }

    private void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password, String userid) {
        this.password = Encryption.getSaltedKey(password, userid);
    }
}
