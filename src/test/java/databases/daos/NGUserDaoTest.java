package databases.daos;

import kotori.DBConfig;
import databases.resources.DBNGUserResource;
import databases.entities.NGUser;
import helper.DaoImplHelper;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class NGUserDaoTest {
    @Rule
    public final DBNGUserResource resource = new DBNGUserResource();
    private final NGUserDao dao = DaoImplHelper.get(NGUserDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    @Test
    public void 用意したテストデータを全件取得できる() throws Exception {
        tm.required(() -> {
            // exercise
            List<NGUser> ngUsers = dao.select(SelectOptions.get());
            NGUser ngUser = ngUsers.get(0);

            // verify
            assertNotNull(ngUsers);
            assertThat(ngUsers.size(), greaterThan(1));
            assertThat(ngUser.getId(), is(2));
            assertThat(ngUser.getWord(), is("nguser"));
        });
    }

    @Test
    public void INSERTが問題なく実行できる() throws Exception {
        tm.required(() -> {
            // setup
            NGUser ngUser = new NGUser();
            ngUser.setWord("foo");

            // exercise
            int result = dao.insert(ngUser);

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
