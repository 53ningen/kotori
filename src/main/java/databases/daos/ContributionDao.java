package databases.daos;


import bulletinBoard.DBConfig;
import databases.entities.Contribution;
import models.payloads.UpdatePayload;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;
import java.util.Optional;

@Dao(config = DBConfig.class)
public interface ContributionDao {

    @Select
    List<Contribution> findWithLimit(SelectOptions options);

    @Select
    Contribution findById(int id);

    @Select
    List<Contribution> findByKeyword(SelectOptions options, String keyword);

    @Insert
    int insert(Contribution cont);

    @Update(sqlFile = true)
    int updateById(UpdatePayload payload);

    @Delete(sqlFile = true)
    int deleteById(int id);

}
