package databases.daos;

import bulletinBoard.DBConfig;
import databases.entities.User;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;

@Dao(config = DBConfig.class)
public interface UserDao {

    @Insert
    int insert(User user);

    @Delete(sqlFile = true)
    int deleteById(int id);

}
