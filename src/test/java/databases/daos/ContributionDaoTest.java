package databases.daos;

import bulletinBoard.DBConfig;
import databases.DBResource;
import databases.entities.Contribution;
import helper.DaoImplHelper;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ContributionDaoTest {
    @Rule
    public final DBResource resource = new DBResource();
    private final ContributionDao dao = DaoImplHelper.get(ContributionDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    @Test
    public void 用意したテストデータをidを指定して取得できる() {
        tm.required(() -> {
            Optional<Contribution> contributionOpt = dao.findById(1);
            Contribution contribution = contributionOpt.orElse(new Contribution());
            assertThat(contribution.getId(), is(1));
            assertThat(contribution.getTitle(), is("hoge"));
            assertThat(contribution.getContent(), is("テスト1"));
            assertThat(contribution.getCreatedAt(), is(LocalDateTime.of(2008, 1, 1, 12, 12, 12)));
        });
    }

    @Test
    public void 用意したテストデータを全件取得できる() {
        tm.required(() -> {
            List<Contribution> contributions = dao.findAll();
            Contribution contribution = contributions.get(1);

            assertNotNull(contributions);
            assertThat(contributions.size(), greaterThan(1));

            assertNotNull(contribution);
            assertThat(contribution.getId(), is(2));
            assertThat(contribution.getTitle(), is("bar"));
        });
    }

    @Test
    public void INSERTが問題なく実行できる() {
        tm.required(() -> {
            Contribution contribution = new Contribution();
            contribution.setTitle("foo");
            contribution.setContent("test");
            contribution.setCreatedAt(LocalDateTime.of(2015, 7, 31, 12, 24, 36));
            int result = dao.insert(contribution);
            assertThat(result, is(1));
        });
    }
}
