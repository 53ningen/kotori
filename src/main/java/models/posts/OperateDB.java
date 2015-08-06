package models.posts;

import bulletinBoard.DBConfig;
import databases.daos.ContributionDao;
import databases.entities.Contribution;
import helper.DaoImplHelper;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.*;

public class OperateDB {
    private final ContributionDao dao = DaoImplHelper.get(ContributionDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    /**
     * 受け取ったContributionをDBに格納する
     * @param contribution Contributionインスタンス
     * @return 処理した投稿数
     */
    public int insertContribution(Contribution contribution) {
        return tm.required(() -> dao.insert(contribution));
    }

    /**
     * pageの位置からlimit分だけ投稿情報をID降順で返す
     * @param page 開始ページ
     * @param limit 表示数
     * @return 投稿リスト
     */
    public List<Contribution> findContributionsWithLimit(int page, int limit)
    {
        SelectOptions options = SelectOptions.get().offset(page * 10).limit(limit).count();
        return tm.required(() -> dao.findWithLimit(options));
    }

}
