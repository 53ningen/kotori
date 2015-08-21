package models.posts.inserts;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.Contribution;
import models.contributions.HandleContribution;
import models.payloads.HandlePayload;
import models.payloads.PostPayload;
import models.posts.handles.HandleDBForNGUser;
import models.posts.utils.ErrorCode;
import models.posts.handles.HandleDBForContribution;
import models.posts.utils.Status;
import models.posts.handles.HandleDBForNGWord;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class InsertContribution extends Status implements InsertInterface {
    private static final InsertContribution insertContribution = new InsertContribution();
    private HandleDBForContribution handleDBForContribution;
    private HandleDBForNGWord handleDBForNGWord;
    private HandleDBForNGUser handleDBForNGUser;
    private HandleContribution handleContribution;

    private InsertContribution() {
        handleDBForContribution = new HandleDBForContribution();
        handleDBForNGWord = new HandleDBForNGWord();
        handleDBForNGUser = new HandleDBForNGUser();
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
            } else if (!HandlePayload.isValidUsername(handleDBForNGUser.findAll(), payload)) {
                return setBadRequest(response, ErrorCode.NGUSER);
            } else if (!HandlePayload.isValidContent(handleDBForNGWord.findAll(), payload)) {
                return setBadRequest(response, ErrorCode.NGWORD_CONTAINS);
            }

            // Contributionを生成する
            Contribution contribution = new Contribution(payload);

            // ContributionがNotNullならばDBに挿入する
            int result = handleDBForContribution.insert(contribution);
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
