package databases.entities;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.LOWER_CASE)
public class NGUser implements NGInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
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
    public void setWord(String username) {
        this.word = username;
    }
}
