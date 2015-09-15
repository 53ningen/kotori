package models.contributions;

import databases.entities.Contribution;
import databases.entities.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HandleContribution {

    /**
     * 全ての投稿に整形した日付と新着投稿かどうかの情報を付与する
     * @param contributions 投稿リスト
     * @param user ユーザ情報
     * @return 情報が付与された投稿リスト
     */
    public List<Contribution> addInformationContributions(List<Contribution> contributions, User user) {
        contributions.forEach(contribution -> addInformationContribution(contribution, user));
        return contributions;
    }

    /**
     * 投稿に整形した日付と新着投稿かどうかの情報を付与する
     * @param contribution 投稿
     * @param user ユーザ情報
     * @return 情報が付与された投稿
     */
    public Contribution addInformationContribution(Contribution contribution, User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ldt = contribution.getCreatedAt();
        contribution.setContent(replaceNewLineToTag(contribution.getContent()));
        contribution.setEditedCreatedTime(ldt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        contribution.setIsNew(now.isAfter(ldt) && now.isBefore(ldt.plusDays(1)));
        if (contribution.getUserid().equals(user.getUserid())) {
            contribution.setIsDeletable(true);
        }
        return contribution;
    }

    /**
     * 改行コードをbrタグに置き換える
     * @param str 改行コードを含む文字列
     * @return 置換された文字列
     */
    private String replaceNewLineToTag(String str) {
        if (str == null) return "";
        else return str.replaceAll("\\\\n", "<br>");
    }

}
