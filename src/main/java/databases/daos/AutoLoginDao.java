package databases.daos;

import bulletinBoard.DBConfig;
import databases.entities.AutoLogin;
import org.seasar.doma.*;

@Dao(config = DBConfig.class)
public interface AutoLoginDao {

    @Select
    AutoLogin selectByToken(String token);

    @Insert
    int insert(AutoLogin data);

    @Update(exclude = {"token", "userid"})
    int update(AutoLogin data);

    @Delete(sqlFile = true)
    int deleteByUserId(String userid);
}
