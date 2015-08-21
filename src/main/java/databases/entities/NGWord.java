package databases.entities;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.LOWER_CASE)
public class NGWord implements NGInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "word")
    private String word;

    @Override
    public boolean isValid() {
        return !word.isEmpty();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public void setWord(String word) {
        this.word = word;
    }
}
