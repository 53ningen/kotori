package databases.entities;

public class SupContribution {
    private Boolean isNew;
    private Boolean isDeletable = false;
    private String editedCreatedTime;

    public Boolean getIsNew() {
        return isNew;
    }

    public Boolean getIsDeletable() {
        return isDeletable;
    }

    public String getEditedCreatedTime() {
        return editedCreatedTime;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew = isNew;
    }

    public void setIsDeletable(Boolean isDeletable) {
        this.isDeletable = isDeletable;
    }

    public void setEditedCreatedTime(String editedCreatedTime) {
        this.editedCreatedTime = editedCreatedTime;
    }
}
