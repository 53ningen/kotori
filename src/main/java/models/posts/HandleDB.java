package models.posts;

import bulletinBoard.DBConfig;
import databases.daos.ContributionDao;
import databases.entities.Contribution;
import helper.DaoImplHelper;
import models.requests.HandleRequest;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.*;

public class HandleDB {
    private final ContributionDao dao = DaoImplHelper.get(ContributionDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();
    private SelectOptions options;

    /**
     * 受け取ったContributionをDBに格納する
     * @param contribution Contributionインスタンス
     * @return 処理した投稿数
     */
    public int insertContribution(Contribution contribution) {
        return tm.required(() -> dao.insert(contribution));
    }

    /**
     * 受け取ったidの投稿をDBから削除する（Admin用）
     * @param id 投稿id
     * @return 処理した投稿数
     */
    public int deleteContribution(int id) {
        return tm.required(() -> dao.deleteById(id));
    }

    /**
     * 受け取ったidの投稿を削除キーを使用してDBから削除する
     * @param id 投稿id
     * @return 処理した投稿数
     */
    public int deleteContributionWithKey(int id, String deleteKey) {
        return tm.required(() -> dao.deleteByIdWithKey(id, deleteKey));
    }

    /**
     * pageの位置からlimit分だけ投稿情報をID降順で返す
     * @param req クエリリクエスト
     * @return 投稿リスト
     */
    public List<Contribution> findContributionsWithLimit(HandleRequest req)
    {
        options = createOptions(req);
        return tm.required(() -> dao.findWithLimit(options));
    }

    /**
     * 指定されたキーワードを含む投稿情報をID降順で返す
     * @param req クエリリクエスト
     * @return 投稿リスト
     */
    public List<Contribution> findContributionsByKeyword(HandleRequest req) {
        options = createOptions(req);
        return tm.required(() -> dao.findByKeyword(options, req.getQuery()));
    }

    // TODO: SelectOptionsは別クラスに分離しておきたい

    /**
     * SelectOptionsを作成する
     * @param req クエリリクエスト
     * @return SelectOptions
     */
    private SelectOptions createOptions(HandleRequest req) {
        int page = req.getPage() - 1;
        int limit = req.getLimit();
        return SelectOptions.get().offset(page * limit).limit(limit).count();
    }

    /**
     * DBに格納されている投稿件数を返す
     * @return 投稿件数
     */
    public long getContributionCounts() {
        try {
            return options.getCount();
        } catch (NullPointerException e) {
            return 0;
        }
    }

}
