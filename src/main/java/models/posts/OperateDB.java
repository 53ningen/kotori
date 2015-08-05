package models.posts;

import bulletinBoard.DBConfig;
import databases.daos.ContributionDao;
import databases.entities.Contribution;
import helper.DaoImplHelper;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class OperateDB {
    private final ContributionDao dao = DaoImplHelper.get(ContributionDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    /**
     * 受け取ったPayloadを基にContributionを生成する
     * @param payload PostPayloadインスタンス
     * @return Contributionインスタンス
     */
    public Optional<Contribution> createContribution(Payload payload) {
        Contribution contribution = new Contribution();
        contribution.setUsername(payload.getUsername());
        contribution.setTitle(payload.getTitle());
        contribution.setContent(payload.getContent());
        contribution.setCreatedAt(LocalDateTime.now());
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

    /**
     * 全ての投稿に整形した日付と新着投稿かどうかの情報を付与する
     * @param contributions 投稿リスト
     * @return 情報が付与された投稿リスト
     */
    public List<Contribution> addInformationContributions(List<Contribution> contributions) {
        contributions.forEach(this::addInformationContribution);
        return contributions;
    }

    /**
     * 投稿に整形した日付と新着投稿かどうかの情報を付与する
     * @param contribution 投稿
     * @return 情報が付与された投稿
     */
    public Contribution addInformationContribution(Contribution contribution) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ldt = contribution.getCreatedAt();
        contribution.setEditedCreatedTime(ldt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        contribution.setIsNew(now.isAfter(ldt) && now.isBefore(ldt.plusDays(1)));
        return contribution;
    }
}
