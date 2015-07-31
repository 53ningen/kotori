package databases.daos;


import bulletinBoard.DBConfig;
import databases.entities.Contribution;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;

import java.util.List;

@Dao(config = DBConfig.class)
public interface ContributionDao {

    @Select
    List<Contribution> findAll();

    @Select
    Contribution find(int id);

    @Insert
    int insert(Contribution cont);
}
