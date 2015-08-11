package databases.daos;


import bulletinBoard.DBConfig;
import databases.entities.Contribution;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;
import java.util.Optional;

@Dao(config = DBConfig.class)
public interface ContributionDao {

    @Select
    List<Contribution> findWithLimit(SelectOptions options);

    @Select
    Optional<Contribution> findById(int id);

    @Select
    List<Contribution> findByKeyword(SelectOptions options, String keyword);

    @Insert
    int insert(Contribution cont);

    @Delete(sqlFile = true)
    int deleteById(int id, String deleteKey);

}
