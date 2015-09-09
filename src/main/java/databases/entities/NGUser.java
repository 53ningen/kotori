package databases.entities;

import org.seasar.doma.*;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.LOWER_CASE)
public class NGUser implements NGInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userid")
    private String userid;

    @Override
    public boolean isValid() {
        return !userid.isEmpty();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getWord() {
        return userid;
    }

    @Override
    public void setWord(String userid) {
        this.userid = userid;
    }
}
