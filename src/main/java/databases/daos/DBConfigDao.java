package databases.daos;

import bulletinBoard.DBConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Script;

@Dao(config = DBConfig.class)
public interface DBConfigDao {

    @Script
    void create();

    @Script
    void drop();

}
