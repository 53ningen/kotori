package databases.daos;

import bulletinBoard.DBConfig;
import databases.entities.User;
import databases.resources.DBUserResource;
import helper.DaoImplHelper;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.tx.TransactionManager;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest {
    @Rule
    public final DBUserResource resource = new DBUserResource();
    private final UserDao dao = DaoImplHelper.get(UserDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    @Test
    public void INSERTが問題なく実行できる() throws Exception {
        tm.required(() -> {
            // setup
            User user = new User("hoge", "hoge");

            // exercise
            int result = dao.insert(user);

            // verify
            assertThat(result, is(1));
        });
    }

    @Test
    public void IDによるdeleteが正しく実行される() throws Exception {
        tm.required(() -> {
            // exercise
            int result = dao.deleteById(1);

            // verify
            assertThat(result, is(1));
        });
    }
}
