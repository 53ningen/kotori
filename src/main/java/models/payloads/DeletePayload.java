package models.payloads;

public class DeletePayload {
    private int id;
    private String username;
    private String deleteKey;

    public boolean isValid() {
        return id > 0 && !deleteKey.isEmpty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeleteKey() {
        return deleteKey;
    }

    public void setDeleteKey(String deleteKey) {
        this.deleteKey = deleteKey;
    }
}
