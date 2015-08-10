package models.contributions;

import databases.entities.Contribution;
import models.payloads.PostPayload;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandleContribution {

    /**
     * 受け取ったPayloadを基にContributionを生成する
     * @param payload PostPayloadインスタンス
     * @return Contributionインスタンス
     */
    public Optional<Contribution> createContribution(PostPayload payload) {
        Contribution contribution = new Contribution();
        contribution.setUsername(payload.getUsername());
        contribution.setTitle(payload.getTitle());
        contribution.setContent(payload.getContent());
        contribution.setDeleteKey(Encryption.getSaltedDeleteKey(payload.getDeleteKey(), payload.getUsername()));
        contribution.setCreatedAt(LocalDateTime.now());
        return Optional.of(contribution);
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
        contribution.setContent(replaceNewLineToTag(contribution.getContent()));
        contribution.setEditedCreatedTime(ldt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        contribution.setIsNew(now.isAfter(ldt) && now.isBefore(ldt.plusDays(1)));
        return contribution;
    }

    /**
     * unicodeエスケープされた文字列を元に戻す
     * @param body unicodeが含まれる文字列
     * @return アンエスケープした文字列
     */
    public String unescapeUnicode(String body) {
        String regex = "\\\\\\\\u([a-fA-F0-9]{4})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(body);

        StringBuffer strBuf = new StringBuffer();
        while (m.find()) {
            if (m.group(1).matches("000a|000d"))
                m.appendReplacement(strBuf, "\\\\\\\\n"); // 改行、ラインフィールドは改行コードとして保存する
            else
                m.appendReplacement(strBuf, Matcher.quoteReplacement(String.valueOf((char) Integer.parseInt(m.group(1), 16))));
        }
        m.appendTail(strBuf);

        return validateBody(strBuf.toString());
    }

    /**
     * タグを削除した文字列を返す
     * @param body タグが含まれる文字列
     * @return タグを削除した文字列
     */
    private String validateBody(String body) {
        return body.replaceAll("<(\".*?\"|'.*?'|[^'\"])*?>", "");
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
