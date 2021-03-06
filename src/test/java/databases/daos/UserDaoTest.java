package databases.daos;

import kotori.DBConfig;
import databases.entities.User;
import databases.resources.DBAdminResource;
import databases.resources.DBUserResource;
import helper.DaoImplHelper;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class UserDaoTest {
    @Rule
    public final DBUserResource userResource = new DBUserResource();
    @Rule
    public final DBAdminResource adminResource = new DBAdminResource();
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
    public void admin権限を持っているuseridの場合Userインスタンスが返る() throws Exception {
        tm.required(() -> {
            // setup
            String userid = "hanayo";

            // exercise
            Optional<User> user = dao.selectAdminUser(userid);

            // verify
            assertTrue(user.isPresent());
            assertThat(user.get().getUsername(), is("小泉花陽"));
        });
    }

    @Test
    public void admin権限を持っていないuseridの場合空のOptionalが返る() throws Exception {
        tm.required(() -> {
            // setup
            String userid = "testuser";

            // exercise
            Optional<User> user = dao.selectAdminUser(userid);

            // verify
            assertThat(user, is(Optional.empty()));
        });
    }

    @Test
    public void useridを指定するとusernameが返る() throws Exception {
        tm.required(() -> {
            // setup
            String userid = "hanayo";

            // exercise
            String username = dao.selectUsername(userid);

            // verify
            assertThat(username, is("小泉花陽"));
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
