package databases.entities;

public class SupContribution {
    private Boolean isNew;
    private String editedCreatedTime;

    public Boolean getIsNew() {
        return isNew;
    }

    public String getEditedCreatedTime() {
        return editedCreatedTime;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public void setEditedCreatedTime(String editedCreatedTime) {
        this.editedCreatedTime = editedCreatedTime;
    }
}
