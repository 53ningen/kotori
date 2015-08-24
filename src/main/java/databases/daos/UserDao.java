package databases.daos;

import bulletinBoard.DBConfig;
import databases.entities.User;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.jdbc.UniqueConstraintException;

@Dao(config = DBConfig.class)
public interface UserDao {

    @Insert
    int insert(User user) throws UniqueConstraintException;

    @Delete(sqlFile = true)
    int delete(User user);

    @Delete(sqlFile = true)
    int deleteById(int id);
}
