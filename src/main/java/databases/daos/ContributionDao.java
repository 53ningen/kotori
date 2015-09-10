package databases.daos;


import bulletinBoard.DBConfig;
import databases.entities.Contribution;
import models.payloads.DeletePayload;
import models.payloads.UpdatePayload;
import org.seasar.doma.*;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;
import java.util.Optional;

@Dao(config = DBConfig.class)
public interface ContributionDao {

    @Select
    List<Contribution> selectWithLimit(SelectOptions options);

    @Select
    Contribution selectById(int id);

    @Select
    List<Contribution> selectByKeyword(SelectOptions options, String keyword);

    @Select
    List<Contribution> select(SelectOptions options, String userid);

    @Insert
    int insert(Contribution cont);

    @Update(sqlFile = true)
    int updateById(UpdatePayload payload);

    @Delete(sqlFile = true)
    int deleteById(int id);

    @Delete(sqlFile = true)
    int delete(DeletePayload payload);

}
