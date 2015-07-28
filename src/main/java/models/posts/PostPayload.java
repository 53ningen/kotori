package models.posts;

public class PostPayload {
    private String id;
    private String content;

    public boolean isValid() {
        return !content.isEmpty();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
