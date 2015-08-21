package databases.resources;

import bulletinBoard.DBConfig;
import databases.daos.DBConfigDao;
import helper.DaoImplHelper;
import org.junit.rules.ExternalResource;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class DBUserResource extends ExternalResource {

    private DBConfigDao dao = DaoImplHelper.get(DBConfigDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    @Override
    protected void before() throws Throwable {
        tm.required(dao::createUser);
    }

    @Override
    protected void after() {
        tm.required(dao::dropUser);
    }
}
