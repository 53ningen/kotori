package databases.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.posts.utils.CSRFToken;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.jdbc.entity.NamingType;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(naming = NamingType.LOWER_CASE)
public class AutoLogin {

    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "userid")
    private String userid;

    @Column(name = "expire")
    private Timestamp expire;

    public AutoLogin() {}

    public AutoLogin(String token, String userid, LocalDateTime ldt) {
        setToken(token);
        setUserid(userid);
        setExpire(ldt);
    }

    @JsonCreator
    public AutoLogin(@JsonProperty("userid") String userid) {
        setToken(CSRFToken.getCSRFToken());
        setUserid(userid);
        setExpire(LocalDateTime.now().plusWeeks(1));
    }

    public String getToken() {
        return token;
    }

    private void setToken(String token) {
        this.token = token;
    }

    public String getUserid() {
        return userid;
    }

    private void setUserid(String userid) {
        this.userid = userid;
    }

    public LocalDateTime getExpire() {
        return expire.toLocalDateTime();
    }

    private void setExpire(LocalDateTime ldt) {
        this.expire = Timestamp.valueOf(ldt);
    }
}
