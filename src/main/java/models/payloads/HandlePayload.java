package models.payloads;

import databases.entities.NGInterface;
import databases.entities.NGWord;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlePayload {

    /**
     * 投稿内容にNGワードが含まれていないか判定する
     * @param postPayload 投稿内容
     * @return boolean
     */
    public static boolean isValidContent(List<NGWord> ngWords, PostPayload postPayload) {
        if (ngWords.isEmpty()) return true;
        else if (containsNGWord(ngWords, postPayload.getTitle()) ||
                 containsNGWord(ngWords, postPayload.getContent())) return false;
        return true;
    }

    /**
     * unicodeエスケープされた文字列を元に戻す
     * @param body unicodeが含まれる文字列
     * @return アンエスケープした文字列
     */
    public static String unescapeUnicode(String body) {
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
     * 文字列にNGワードが含まれているか判定する
     * @param wordList NGワードリスト
     * @param word 文字列
     * @return boolean
     */
    private static <T extends NGInterface> boolean containsNGWord(List<T> wordList, String word) {
        return wordList.stream().anyMatch(ngWord -> word.contains(ngWord.getWord()));
    }

    /**
     * タグを削除した文字列を返す
     * @param body タグが含まれる文字列
     * @return タグを削除した文字列
     */
    private static String validateBody(String body) {
        return body.replaceAll("<(\".*?\"|'.*?'|[^'\"])*?>", "");
    }
}
