package databases.daos;


import bulletinBoard.DBConfig;
import databases.entities.Contribution;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;

import java.util.List;
import java.util.Optional;

@Dao(config = DBConfig.class)
public interface ContributionDao {

    @Select
    List<Contribution> findAll();

    @Select
    Optional<Contribution> find(int id);

    @Insert
    int insert(Contribution cont);
}
