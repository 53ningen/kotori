package models.posts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.Contribution;
import models.contributions.HandleContribution;
import models.payloads.PostPayload;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class PostContribution extends Status {
    private HandleDB handleDB = new HandleDB();
    private HandleContribution handleContribution = new HandleContribution();

    /**
     * postによる投稿を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    public String requestPostContribution(Request request, Response response) {

        try {
            // postPayloadを生成する
            PostPayload payload = new ObjectMapper().readValue(handleContribution.unescapeUnicode(request.body()), PostPayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response);
            }

            // OptionalなContributionを生成する
            Optional<Contribution> contributionOpt = handleContribution.createContribution(payload);

            // ContributionがNotNullならばDBに挿入する
            int result = handleDB.insertContribution(contributionOpt.get());
            if (result < 1) {
                return setBadRequest(response);
            }

            // Contributionに新着情報を付与する
            Contribution contribution = handleContribution.addInformationContribution(contributionOpt.get());

            // ステータスコード200 OKを設定する
            setOK(response);
            response.type("application/json");

            return convertContributionToJson(contribution);
        } catch (Exception e) {
            // ステータスコード400 BadRequestを設定する
            return setBadRequest(response);
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
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        return mapper.writeValueAsString(contribution);
    }
}
