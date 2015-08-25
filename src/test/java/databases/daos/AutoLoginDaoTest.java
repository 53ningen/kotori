package databases.daos;

import bulletinBoard.DBConfig;
import databases.entities.AutoLogin;
import databases.entities.Contribution;
import databases.entities.User;
import databases.resources.DBAutoLoginResource;
import databases.resources.DBUserResource;
import helper.DaoImplHelper;
import models.payloads.UpdatePayload;
import models.posts.utils.CSRFToken;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.UniqueConstraintException;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class AutoLoginDaoTest {
    @Rule
    public final DBAutoLoginResource resource = new DBAutoLoginResource();
    private final AutoLoginDao dao = DaoImplHelper.get(AutoLoginDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    @Test
    public void 用意したテストデータをTokenを指定して取得できる() throws Exception {
        tm.required(() -> {
            // exercise
            Optional<AutoLogin> alOpt = dao.selectByToken("hoge");
            AutoLogin al = alOpt.get();

            // verify
            assertThat(al.getToken(), is("hoge"));
            assertThat(al.getUserid(), is("test"));
            assertThat(al.getExpire(), is(LocalDateTime.of(2009, 1, 1, 12, 11, 13)));
        });
    }

    @Test
    public void INSERTが問題なく実行できる() throws Exception {
        tm.required(() -> {
            // setup
            AutoLogin al = new AutoLogin(CSRFToken.getCSRFToken(), "hoge", LocalDateTime.now());

            // exercise
            int result = dao.insert(al);

            // verify
            assertThat(result, is(1));
        });
    }

    @Test
    public void tokenが既に存在する場合主キー違反でエラーが返る() throws Exception {
        tm.required(() -> {
            // setup
            AutoLogin al = new AutoLogin("hoge", "test", LocalDateTime.now());

            // exercise
            int result;
            try {
                result = dao.insert(al);
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
            AutoLogin al = new AutoLogin("hoge", "test", LocalDateTime.of(2009, 1, 1, 12, 11, 13));
            // exercise
            int result = dao.delete(al);

            // verify
            assertThat(result, is(1));
        });
    }

    @Test
    public void idによるdeleteが正しく実行される() throws Exception {
        tm.required(() -> {
            // exercise
            int result = dao.deleteByUserId("test");

            // verify
            assertThat(result, is(1));
        });
    }
}
