package databases.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import models.posts.utils.Encryption;
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

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @JsonIgnore
    @Column(name = "delete_key")
    private String deleteKey;

    @JsonIgnore
    @Column(name = "created_at")
    private Timestamp createdAt;

    public Contribution() {}

    public Contribution(PostPayload payload) {
        setUsername(payload.getUsername());
        setTitle(payload.getTitle());
        setContent(payload.getContent());
        setDeleteKey(Encryption.getSaltedKey(payload.getDeleteKey(), payload.getUsername()));
        setCreatedAt(LocalDateTime.now());
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDeleteKey() {
        return deleteKey;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt.toLocalDateTime();
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private void setDeleteKey(String deleteKey) {
        this.deleteKey = deleteKey;
    }

    private void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = Timestamp.valueOf(createdAt);
    }
}

