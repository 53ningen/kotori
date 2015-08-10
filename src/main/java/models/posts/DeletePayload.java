package models.posts;

public class DeletePayload {
    private int id;
    private String pass;

    public boolean isValid() {
        return id > 0 && !pass.isEmpty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
