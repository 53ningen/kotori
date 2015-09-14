package kotori;

import databases.daos.DBConfigDao;
import helper.DaoImplHelper;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class MySQL {
    private static final DBConfigDao dao = DaoImplHelper.get(DBConfigDao.class);
    private static final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    protected static void setupDatabase() {
        tm.required(dao::createDatabase);
    }
}
