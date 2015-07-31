package models.posts;

import bulletinBoard.DBConfig;
import databases.daos.ContributionDao;
import databases.entities.Contribution;
import helper.DaoImplHelper;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.time.LocalDateTime;
import java.util.*;

public class PostDB {
    private static PostDB postDB = new PostDB();
    private final ContributionDao dao = DaoImplHelper.get(ContributionDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();
    public static PostDB getPostDB() {
        return postDB;
    }

    /**
     * 受け取ったPayloadを基にContributionを生成する
     * @param payload PostPayloadインスタンス
     * @return Contributionインスタンス
     */
    public Optional<Contribution> createContribution(PostPayload payload) {
        Contribution contribution = new Contribution();
        contribution.setCreatedAt(LocalDateTime.now());
        contribution.setTitle(payload.getTitle());
        contribution.setContent(payload.getContent());
        return Optional.of(contribution);
    }

    /**
     * 受け取ったContributionをDBに格納する
     * @param contribution Contributionインスタンス
     * @return 処理した投稿数
     */
    public int insertContribution(Contribution contribution) {
        return tm.required(() -> dao.insert(contribution));
    }

    /**
     * 全ての投稿をID降順で返す
     * @return 投稿リスト
     */
    public List<Contribution> findAllContributions()
    {
        return tm.required(dao::findAll);
    }
}
