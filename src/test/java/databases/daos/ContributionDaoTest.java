package databases.daos;

import bulletinBoard.DBConfig;
import databases.DBResource;
import databases.entities.Contribution;
import helper.DaoImplHelper;
import models.payloads.UpdatePayload;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.SelectOptions;
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
    public void 用意したテストデータをidを指定して取得できる() throws Exception {
        tm.required(() -> {
            // exercise
            Optional<Contribution> contributionOpt = dao.findById(1);
            Contribution contribution = contributionOpt.orElse(new Contribution());

            // verify
            assertThat(contribution.getId(), is(1));
            assertThat(contribution.getUsername(), is("小泉花陽"));
            assertThat(contribution.getTitle(), is("hoge"));
            assertThat(contribution.getContent(), is("テスト1"));
            assertThat(contribution.getCreatedAt(), is(LocalDateTime.of(2008, 1, 1, 12, 12, 12)));
        });
    }

    @Test
    public void 用意したテストデータを全件取得できる() throws Exception {
        tm.required(() -> {
            // setup
            SelectOptions options = SelectOptions.get().offset(0).limit(10);

            // exercise
            List<Contribution> contributions = dao.findWithLimit(options);
            Contribution contribution = contributions.get(1);

            // verify
            assertNotNull(contributions);
            assertThat(contributions.size(), greaterThan(1));
            assertThat(contribution.getId(), is(2));
            assertThat(contribution.getTitle(), is("bar"));
        });
    }

    @Test
    public void INSERTが問題なく実行できる() throws Exception {
        tm.required(() -> {
            // setup
            Contribution contribution = new Contribution();
            contribution.setUsername("高坂穂乃果");
            contribution.setTitle("foo");
            contribution.setContent("test");
            contribution.setDeleteKey("pass");
            contribution.setCreatedAt(LocalDateTime.of(2015, 7, 31, 12, 24, 36));

            // exercise
            int result = dao.insert(contribution);

            // verify
            assertThat(result, is(1));
        });
    }

    @Test
    public void キーワード検索の結果が正しく取得できる() throws Exception {
        tm.required(() -> {
            // setup
            SelectOptions options = SelectOptions.get().offset(0).limit(10);
            String keyword = "%テスト%";

            // exercise
            List<Contribution> contributions = dao.findByKeyword(options, keyword);

            // verify
            assertThat(contributions.size(), is(3));
            assertThat(contributions.get(0).getContent(), containsString("テスト"));
        });
    }

    @Test
    public void limit分だけキーワード検索の結果が取得できる() throws Exception {
        tm.required(() -> {
            // setup
            SelectOptions options = SelectOptions.get().offset(0).limit(1);
            String keyword = "%テスト%";

            // exercise
            List<Contribution> contributions = dao.findByKeyword(options, keyword);

            // verify
            assertThat(contributions.size(), is(1));
        });
    }

    @Test
    public void UPDATEが問題なく実行できる() throws Exception {
        tm.required(() -> {
            // setup
            UpdatePayload payload = new UpdatePayload();
            payload.setContent("南ことり");

            // exercise
            int result = dao.updateById(payload, 1);
            Optional<Contribution> resultContributionOpt = dao.findById(1);
            Contribution resultContribution = resultContributionOpt.get();

            // verify
            assertThat(result, is(1));
            assertThat(resultContribution.getContent(), is("南ことり"));
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

    @Test
    public void 削除キーによるdeleteが正しく実行される() throws Exception {
        tm.required(() -> {
            // exercise
            int result = dao.deleteByIdWithKey(2, "pass");

            // verify
            assertThat(result, is(1));
        });
    }


}
