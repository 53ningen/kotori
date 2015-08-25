package databases.daos;

import bulletinBoard.DBConfig;
import databases.entities.AutoLogin;
import org.seasar.doma.*;

import java.util.Optional;

@Dao(config = DBConfig.class)
public interface AutoLoginDao {

    @Select
    Optional<AutoLogin> selectByToken(String token);

    @Insert
    int insert(AutoLogin al);

    @Delete(sqlFile = true)
    int deleteByUserId(String userid);

    @Delete
    int delete(AutoLogin al);
}
