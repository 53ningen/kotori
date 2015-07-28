package models.posts;

public class PostPayload {
    private String title;
    private String content;

    public boolean isValid() {
        if (title.isEmpty() || title.length() > 20) return false;
        if (content.isEmpty()) return false;

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
