package models.posts;

public class PostPayload {
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
}
