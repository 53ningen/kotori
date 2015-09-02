package databases.daos;

import bulletinBoard.DBConfig;
import databases.entities.User;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.jdbc.UniqueConstraintException;

import java.util.Optional;

@Dao(config = DBConfig.class)
public interface UserDao {

    @Insert
    int insert(User user) throws UniqueConstraintException;

    @Select
    Optional<User> select(String userid, String password);

    @Select
    String selectUsername(String userid);

    @Delete(sqlFile = true)
    int delete(User user);
}
