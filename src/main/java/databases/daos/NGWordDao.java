package databases.daos;

import bulletinBoard.DBConfig;
import databases.entities.NGWord;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

@Dao(config = DBConfig.class)
public interface NGWordDao {

    @Select
    List<NGWord> select(SelectOptions options);

    @Insert
    int insert(NGWord word);

    @Delete(sqlFile = true)
    int deleteById(int id);

}
