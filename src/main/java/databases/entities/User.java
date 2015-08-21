package databases.entities;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.LOWER_CASE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name ="userid")
    private String userid;

    public User() {}

    public User(String username, String userid) {
        setUsername(username);
        setUserid(userid);
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
}
