package databases.daos;

import bulletinBoard.DBConfig;
import databases.DBNGWordResource;
import databases.entities.NGWord;
import helper.DaoImplHelper;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class NGWordDaoTest {
    @Rule
    public final DBNGWordResource resource = new DBNGWordResource();
    private final NGWordDao dao = DaoImplHelper.get(NGWordDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    @Test
    public void 用意したテストデータを全件取得できる() throws Exception {
        tm.required(() -> {
            // exercise
            List<NGWord> ngWords = dao.findAll();
            NGWord ngWord = ngWords.get(0);

            // verify
            assertNotNull(ngWords);
            assertThat(ngWords.size(), greaterThan(1));
            assertThat(ngWord.getId(), is(2));
            assertThat(ngWord.getWord(), is("fuga"));
        });
    }

    @Test
    public void INSERTが問題なく実行できる() throws Exception {
        tm.required(() -> {
            // setup
            NGWord ngWord = new NGWord();
            ngWord.setWord("fuga");

            // exercise
            int result = dao.insert(ngWord);

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
