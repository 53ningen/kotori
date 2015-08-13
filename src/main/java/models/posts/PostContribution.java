package models.posts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.Contribution;
import databases.entities.NGWord;
import models.contributions.HandleContribution;
import models.payloads.HandlePayload;
import models.payloads.PostPayload;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class PostContribution extends Status {
    private HandleDB handleDB = new HandleDB();
    private HandleContribution handleContribution = new HandleContribution();
    private final String RESPONSE_TYPE_JSON = "application/json";

    /**
     * postによるNGワード追加を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    public String requestPostNGWord(Request request, Response response) {

        try {
            NGWord ngWord = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), NGWord.class);
            if (!ngWord.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = handleDB.insertNGWord(ngWord);
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response, RESPONSE_TYPE_JSON);

            return convertObjectToJson(ngWord);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * postによる投稿を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    public String requestPostContribution(Request request, Response response) {

        try {
            // postPayloadを生成する
            PostPayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), PostPayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            } else if (!HandlePayload.isValidContent(handleDB.findAllNGWords(), payload)) {
                return setBadRequest(response, ErrorCode.NGWORD_CONTAINS);
            }

            // OptionalなContributionを生成する
            Optional<Contribution> contributionOpt = handleContribution.createContribution(payload);

            // ContributionがNotNullならばDBに挿入する
            int result = handleDB.insertContribution(contributionOpt.get());
            if (result < 1) {
                return setInternalServerError(response);
            }

            // Contributionに新着情報を付与する
            Contribution contribution = handleContribution.addInformationContribution(contributionOpt.get());

            // ステータスコード200 OKを設定する
            setOK(response, RESPONSE_TYPE_JSON);

            return convertObjectToJson(contribution);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * 投稿情報をjson文字列に変換する
     * @param object 投稿情報
     * @return json文字列
     * @throws JsonProcessingException
     */
    private <T> String convertObjectToJson(T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        return mapper.writeValueAsString(object);
    }
}
