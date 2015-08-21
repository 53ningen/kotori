package databases.entities;

public interface NGInterface {
    boolean isValid();
    int getId();
    String getWord();
    void setWord(String word);
}
