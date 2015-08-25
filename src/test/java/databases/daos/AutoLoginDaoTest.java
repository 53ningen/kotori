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
            AutoLogin al = dao.selectByToken("hoge");

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
    public void UPDATEが問題なく実行できる() throws Exception {
        tm.required(() -> {
            // setup
            LocalDateTime ldt = LocalDateTime.of(2015, 8, 25, 12, 32, 28);
            AutoLogin al = new AutoLogin("hoge", "test", ldt);

            // exercise
            int result = dao.update(al);
            al = dao.selectByToken("hoge");

            // verify
            assertThat(result, is(1));
            assertThat(al.getExpire(), is(ldt));
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
