package models.posts.inserts;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.Contribution;
import models.contributions.HandleContribution;
import models.payloads.HandlePayload;
import models.payloads.PostPayload;
import models.posts.utils.ErrorCode;
import models.posts.handles.HandleDBForContribution;
import models.posts.utils.Status;
import models.posts.handles.HandleDBForNGWord;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class InsertContribution extends Status implements InsertInterface {
    private HandleDBForContribution handleDBForContribution = new HandleDBForContribution();
    private HandleDBForNGWord handleDBForNGWord = new HandleDBForNGWord();
    private HandleContribution handleContribution = new HandleContribution();

    /**
     * postによる投稿を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    @Override
    public String requestInsert(Request request, Response response) {

        try {
            // postPayloadを生成する
            PostPayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), PostPayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            } else if (!HandlePayload.isValidContent(handleDBForNGWord.findAll(), payload)) {
                return setBadRequest(response, ErrorCode.NGWORD_CONTAINS);
            }

            // OptionalなContributionを生成する
            Optional<Contribution> contributionOpt = handleContribution.createContribution(payload);

            // ContributionがNotNullならばDBに挿入する
            int result = handleDBForContribution.insert(contributionOpt.get());
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
}
