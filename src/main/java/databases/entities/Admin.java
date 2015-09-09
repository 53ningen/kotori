package databases.entities;

import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.jdbc.entity.NamingType;

@Entity(naming = NamingType.LOWER_CASE)
public class Admin {

    @Id
    @Column(name = "userid")
    private String userid;

}
