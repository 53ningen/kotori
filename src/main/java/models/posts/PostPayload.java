package models.posts;

public class PostPayload {
    private final int LIMIT_NAME_AND_TITLE_LENGTH = 20;
    private final int LIMIT_CONTENT_LENGTH = 140;
    private String username;
    private String title;
    private String content;
    private String deleteKey;

    public boolean isValid() {
        if (username.isEmpty() || username.length() > LIMIT_NAME_AND_TITLE_LENGTH) return false;
        else if (title.isEmpty() || title.length() > LIMIT_NAME_AND_TITLE_LENGTH) return false;
        else if (content.isEmpty() || content.length() > LIMIT_CONTENT_LENGTH) return false;
        else if (deleteKey.isEmpty()) return false;

        return true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDeleteKey() {
        return deleteKey;
    }

    public void setDeleteKey(String deleteKey) {
        this.deleteKey = deleteKey;
    }
}
