package databases.daos;

import bulletinBoard.DBConfig;
import databases.entities.User;
import databases.resources.DBUserResource;
import helper.DaoImplHelper;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.UniqueConstraintException;
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
            User user = new User("fuga", "hoge", "password");

            // exercise
            int result = dao.insert(user);

            // verify
            assertThat(result, is(1));
        });
    }

    @Test
    public void useridが既に存在する場合主キー違反でエラーが返る() throws Exception {
        tm.required(() -> {
            // setup
            User user = new User("hanayo", "username", "password");

            // exercise
            int result;
            try {
                result = dao.insert(user);
            } catch (UniqueConstraintException e) {
                result = 0;
            }

            // verify
            assertThat(result, is(0));
        });
    }

    @Test
    public void deleteが正しく実行される() throws Exception {
        tm.required(() -> {
            // setup
            User user = new User("hanayo", "小泉花陽", "password");

            // exercise
            int result = dao.delete(user);

            // verify
            assertThat(result, is(1));
        });
    }
}
