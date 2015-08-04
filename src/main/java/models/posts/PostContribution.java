package models.posts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.Contribution;
import spark.Request;
import spark.Response;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostContribution {
    private final int HTTP_BAD_REQUEST = 400;
    private OperateDB operateDB = new OperateDB();

    /**
     * postによる投稿を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    public String requestPostContribution(Request request, Response response) {

        try {
            // postPayloadを生成する
            Payload payload = new ObjectMapper().readValue(unescapeUnicode(request.body()), Payload.class);
            if (!payload.isValid()) {
                return sendBadRequest(response);
            }

            // OptionalなContributionを生成する
            Optional<Contribution> contributionOpt = operateDB.createContribution(payload);

            // ContributionがNotNullならばDBに挿入する
            int result = operateDB.insertContribution(contributionOpt.get());
            if (result < 1) {
                return sendBadRequest(response);
            }

            // Contributionに新着情報を付与する
            Contribution contribution = operateDB.addInformationContribution(contributionOpt.get());

            // ステータスコード200 OKを設定する
            response.status(200);
            response.type("application/json");

            return convertContributionToJson(contribution);
        } catch (Exception e) {
            // ステータスコード400 BadRequestを設定する
            return sendBadRequest(response);
        }
    }

    /**
     * unicodeエスケープされた文字列を元に戻す
     * @param body unicodeが含まれる文字列
     * @return アンエスケープした文字列
     */
    private String unescapeUnicode(String body) {
        String regex = "\\\\\\\\u([a-fA-F0-9]{4})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(body);

        StringBuffer strBuf = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(strBuf, String.valueOf((char) Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(strBuf);

        return strBuf.toString();
    }

    /**
     * 投稿情報をjson文字列に変換する
     * @param contribution 投稿情報
     * @return json文字列
     * @throws JsonProcessingException
     */
    private String convertContributionToJson(Contribution contribution) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(contribution);
    }

    /**
     * 400 BadRequest を設定する
     * @param response レスポンス
     * @return 空文字
     */
    private String sendBadRequest(Response response) {
        response.status(HTTP_BAD_REQUEST);
        return "";
    }
}
