package databases.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.utils.CSRFToken;

public class AutoLogin {

    private String token;

    private String userid;

    private String username;

    @JsonCreator
    public AutoLogin(@JsonProperty("userid") String userid,
                     @JsonProperty("username") String username) {
        setToken(userid + "_" + CSRFToken.getCSRFToken());
        setUserid(userid);
        setUsername(username);
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

    public String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }
}
