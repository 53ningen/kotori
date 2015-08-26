package databases.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.posts.utils.CSRFToken;

public class AutoLogin {

    private String token;

    private String userid;

    @JsonCreator
    public AutoLogin(@JsonProperty("userid") String userid) {
        setToken(userid + "_" + CSRFToken.getCSRFToken());
        setUserid(userid);
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
}
