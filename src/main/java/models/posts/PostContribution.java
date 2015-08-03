package models.posts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.Contribution;
import spark.Request;
import spark.Response;

import java.util.Optional;

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
            Payload payload = new ObjectMapper().readValue(request.body(), Payload.class);
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
     * 投稿情報をjson文字列に変換する
     * @param contribution 投稿情報
     * @return json文字列
     * @throws JsonProcessingException
     */
    private String convertContributionToJson(Contribution contribution) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.print(mapper.writeValueAsString(contribution));
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
