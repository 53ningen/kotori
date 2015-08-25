package models.posts.inserts;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.Contribution;
import models.contributions.HandleContribution;
import models.payloads.HandlePayload;
import models.payloads.PostPayload;
import models.posts.handles.HandleDB;
import models.posts.utils.ErrorCode;

import spark.Request;
import spark.Response;

public class InsertContribution implements InsertInterface {
    private static final InsertContribution insertContribution = new InsertContribution();
    private HandleContribution handleContribution;

    private InsertContribution() {
        handleContribution = new HandleContribution();
    }

    public static InsertContribution getInsertContribution() {
        return insertContribution;
    }

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
            } else if (!HandlePayload.isValidUsername(HandleDB.ngUser().findAll(), payload)) {
                return setBadRequest(response, ErrorCode.NGUSER);
            } else if (!HandlePayload.isValidContent(HandleDB.ngWord().findAll(), payload)) {
                return setBadRequest(response, ErrorCode.NGWORD_CONTAINS);
            }

            // Contributionを生成する
            Contribution contribution = new Contribution(payload);

            // ContributionがNotNullならばDBに挿入する
            int result = HandleDB.contribution().insert(contribution);
            if (result < 1) {
                return setInternalServerError(response);
            }

            // Contributionに新着情報を付与する
            Contribution editedContribution = handleContribution.addInformationContribution(contribution);

            // ステータスコード200 OKを設定する
            setOK(response, RESPONSE_TYPE_JSON);

            return convertObjectToJson(editedContribution);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
