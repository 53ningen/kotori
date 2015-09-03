package models.payloads;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeletePayload {
    private int id;
    @JsonIgnore
    private String userid;

    @JsonCreator
    public DeletePayload(@JsonProperty("id") int id) {
        setId(id);
    }

    public boolean isValid() {
        return id > 0 && !userid.isEmpty();
    }

    public boolean isValidWithoutKey() {
        return id > 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
