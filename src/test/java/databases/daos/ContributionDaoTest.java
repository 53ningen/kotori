package databases.daos;

import kotori.DBConfig;
import databases.entities.Contribution;
import databases.entities.User;
import databases.resources.DBContributionResource;
import helper.DaoImplHelper;
import models.payloads.DeletePayload;
import models.payloads.PostPayload;
import models.payloads.UpdatePayload;
import org.junit.Rule;
import org.junit.Test;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ContributionDaoTest {
    @Rule
    public final DBContributionResource resource = new DBContributionResource();
    private final ContributionDao dao = DaoImplHelper.get(ContributionDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    @Test
    public void 用意したテストデータをidを指定して取得できる() throws Exception {
        tm.required(() -> {
            // exercise
            Contribution contribution = dao.selectById(1);

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
            List<Contribution> contributions = dao.selectWithLimit(options);
            Contribution contribution = contributions.get(1);

            // verify
            assertNotNull(contributions);
            assertThat(contributions.size(), greaterThan(1));
            assertThat(contribution.getId(), is(2));
            assertThat(contribution.getTitle(), is("bar"));
        });
    }

    @Test
    public void 用意したテストデータをuseridを指定して取得できる() throws Exception {
        tm.required(() -> {
            // setup
            SelectOptions options = SelectOptions.get().offset(0);

            // exercise
            List<Contribution> contributions = dao.select(options, "hanayo");
            Contribution contribution = contributions.get(0);

            // verify
            assertThat(contribution.getUsername(), is("小泉花陽"));
            assertThat(contribution.getTitle(), is("hoge"));
            assertThat(contribution.getContent(), is("テスト1"));
        });
    }

    @Test
    public void INSERTが問題なく実行できる() throws Exception {
        tm.required(() -> {
            // setup
            PostPayload payload = new PostPayload();
            payload.setTitle("foo");
            payload.setContent("test");
            User user = new User("hanayo", "小泉花陽");
            Contribution contribution = new Contribution(payload, user);

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
            List<Contribution> contributions = dao.selectByKeyword(options, keyword);

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
            List<Contribution> contributions = dao.selectByKeyword(options, keyword);

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
            payload.setId(1);

            // exercise
            int result = dao.updateById(payload);
            Contribution resultContribution = dao.selectById(1);

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
    public void ユーザIDと投稿IDによるdeleteが正しく実行される() throws Exception {
        tm.required(() -> {
            // setup
            DeletePayload payload = new DeletePayload(1);
            payload.setUserid("hanayo");

            // exercise
            int result = dao.delete(payload);

            // verify
            assertThat(result, is(1));
        });
    }

}
