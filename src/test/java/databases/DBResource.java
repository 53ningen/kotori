package databases;

import bulletinBoard.DBConfig;
import databases.daos.DBConfigDao;
import helper.DaoImplHelper;
import org.junit.rules.ExternalResource;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class DBResource extends ExternalResource {

    private DBConfigDao dao = DaoImplHelper.get(DBConfigDao.class);

    @Override
    protected void before() throws Throwable {
        TransactionManager tm = DBConfig.singleton().getTransactionManager();
        tm.required(dao::create);
    }

    @Override
    protected void after() {
        TransactionManager tm = DBConfig.singleton().getTransactionManager();
        tm.required(dao::drop);
    }

}
