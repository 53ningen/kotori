package models.handles;

import bulletinBoard.DBConfig;
import databases.daos.ContributionDao;
import databases.entities.Contribution;
import databases.entities.User;
import helper.DaoImplHelper;
import models.contributions.HandleContribution;
import models.payloads.DeletePayload;
import models.payloads.UpdatePayload;
import models.utils.DBSelectOptions;
import models.requests.HandleRequest;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.Collections;
import java.util.List;

public class HandleDBForContribution {
    private final ContributionDao contributionDao = DaoImplHelper.get(ContributionDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();
    private final HandleContribution handleContribution = new HandleContribution();
    private SelectOptions options;

    /**
     * 受け取ったContributionをDBに格納する
     * @param contribution Contributionインスタンス
     * @return 格納されたContributionインスタンス
     */
    public List<Contribution> insert(Contribution contribution) {
        return tm.required(() -> {
            int result = contributionDao.insert(contribution);
            if (result < 1) {
                return Collections.<Contribution>emptyList();
            }
            options = DBSelectOptions.getDBSelectOptions().setOptions(1);

            return contributionDao.select(options, contribution.getUserid());
        });
    }

    /**
     * 受け取ったidの投稿を更新する（Admin用）
     * @param payload 更新データ
     * @return 処理した投稿数
     */
    public int update(UpdatePayload payload) {
        return tm.required(() -> contributionDao.updateById(payload));
    }

    /**
     * 受け取ったidの投稿をDBから削除する（Admin用）
     * @param id 投稿id
     * @return 処理した投稿数
     */
    public int deleteById(int id) {
        return tm.required(() -> contributionDao.deleteById(id));
    }

    /**
     * 投稿をDBから削除する
     * @param payload Delete用Payloadインスタンス
     * @return 処理した投稿数
     */
    public int delete(DeletePayload payload) {
        return tm.required(() -> contributionDao.delete(payload));
    }

    /**
     * pageの位置からlimit分だけ投稿情報をID降順で返す
     * @param req クエリリクエスト
     * @return 投稿リスト
     */
    public List<Contribution> findWithLimit(HandleRequest req)
    {
        options = DBSelectOptions.getDBSelectOptions().setOptions(req);
        return tm.required(() -> handleContribution.addInformationContributions(contributionDao.selectWithLimit(options)));
    }

    /**
     * 指定されたキーワードを含む投稿情報をID降順で返す
     * @param req クエリリクエスト
     * @return 投稿リスト
     */
    public List<Contribution> findByKeyword(HandleRequest req) {
        options = DBSelectOptions.getDBSelectOptions().setOptions(req);
        return tm.required(() -> handleContribution.addInformationContributions(contributionDao.selectByKeyword(options, req.getQuery())));
    }

    /**
     * 指定されたユーザの投稿情報をID降順で返す
     * @param req クエリリクエスト
     * @param user ユーザ情報
     * @return 投稿リスト
     */
    public List<Contribution> selectByUser(HandleRequest req, User user) {
        options = DBSelectOptions.getDBSelectOptions().setOptions(req);
        return tm.required(() -> handleContribution.addInformationContributions(contributionDao.select(options, user.getUserid())));
    }
}
