package databases.daos;

import kotori.DBConfig;
import org.seasar.doma.Dao;
import org.seasar.doma.Script;

@Dao(config = DBConfig.class)
public interface DBConfigDao {

    @Script
    void createContribution();

    @Script
    void dropContribution();

    @Script
    void createNGWord();

    @Script
    void dropNGWord();

    @Script
    void createNGUser();

    @Script
    void dropNGUser();

    @Script
    void createUser();

    @Script
    void dropUser();

    @Script
    void createAdmin();

    @Script
    void dropAdmin();

}
