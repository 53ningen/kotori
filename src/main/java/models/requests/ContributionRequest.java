package models.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.Contribution;
import databases.entities.NGWord;
import databases.entities.User;
import models.contributions.HandleContribution;
import models.payloads.DeletePayload;
import models.payloads.HandlePayload;
import models.payloads.PostPayload;
import models.payloads.UpdatePayload;
import models.posts.handles.HandleDB;
import models.posts.utils.ErrorCode;
import models.posts.utils.ResponseType;
import models.users.HandleUser;
import org.seasar.doma.jdbc.SelectOptions;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Optional;

public class ContributionRequest implements DBRequest {
    private HandleContribution handleContribution = new HandleContribution();

    /**
     * postによる投稿を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    @Override
    public String insert(Request request, Response response) {

        try {
            // postPayloadを生成する
            PostPayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), PostPayload.class);
            List<NGWord> ngWordList = HandleDB.ngWord().selectAll();
            if (!payload.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            } else if (!HandlePayload.isValidContent(ngWordList, payload)) {
                return setBadRequest(response, ErrorCode.NGWORD_CONTAINS);
            }

            // ログインしているUser情報を取得する
            Optional<User> userOpt = HandleUser.createUser(request);

            Optional<Contribution> contributionOpt = userOpt.map(user -> new Contribution(payload, user))
                                                            .map(cont -> HandleDB.contribution().insert(cont))
                                                            .map(conts -> conts.get(0));
            if (!contributionOpt.isPresent()) {
                return setInternalServerError(response);
            }

            // Contributionに新着情報を付与する
            Contribution editedContribution = handleContribution.addInformationContribution(contributionOpt.get());

            // ステータスコード200 OKを設定する
            setOK(response, ResponseType.APPLICATION_JSON);

            return convertObjectToJson(editedContribution);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * 投稿の削除を受け付ける（Admin用）
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    public String deleteWithoutKey(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), DeletePayload.class);
            if (!payload.isValidWithoutKey()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = HandleDB.contribution().deleteById(payload.getId());
            if (result < 1) {
                return setInternalServerError(response);
            }

            return setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * 投稿の削除を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    @Override
    public String delete(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), DeletePayload.class);

            Optional<User> userOpt = HandleUser.createUser(request);
            userOpt.ifPresent(user -> payload.setUserid(user.getUserid()));

            if(!payload.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = HandleDB.contribution().delete(payload);
            if (result < 1) {
                return setInternalServerError(response);
            }

            return setOK(response);
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
    @Override
    public String update(Request request, Response response) {

        try {
            // UpdatePayloadを生成する
            UpdatePayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), UpdatePayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            // Payloadのパラメータが正しければDBを更新する
            int result = HandleDB.contribution().update(payload);
            if (result < 1) {
                return setInternalServerError(response);
            }

            // ステータスコード200 OKを設定する
            return setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
