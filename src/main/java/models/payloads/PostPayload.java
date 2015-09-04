package models.payloads;

public class PostPayload {
    private final int LIMIT_TITLE_LENGTH = 20;
    private final int LIMIT_CONTENT_LENGTH = 140;
    private String title;
    private String content;

    public boolean isValid() {
        if (title.isEmpty() || title.length() > LIMIT_TITLE_LENGTH) return false;
        else if (content.isEmpty() || content.length() > LIMIT_CONTENT_LENGTH) return false;

        return true;
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
}
