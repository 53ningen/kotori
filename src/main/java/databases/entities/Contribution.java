package databases.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.payloads.PostPayload;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity(naming = NamingType.LOWER_CASE)
public class Contribution extends SupContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "userid")
    private String userid;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @JsonIgnore
    @Column(name = "created_at")
    private Timestamp createdAt;

    public Contribution() {}

    public Contribution(PostPayload payload, User user) {
        setUsername(user.getUsername());
        setUserid(user.getUserid());
        setTitle(payload.getTitle());
        setContent(payload.getContent());
        setCreatedAt(LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getUserid() {
        return userid;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt.toLocalDateTime();
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setUserid(String userid) {
        this.userid = userid;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = Timestamp.valueOf(createdAt);
    }
}

