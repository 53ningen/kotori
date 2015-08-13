package models.posts.deletes;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.contributions.Encryption;
import models.payloads.DeletePayload;
import models.payloads.HandlePayload;
import models.posts.ErrorCode;
import models.posts.HandleDB;
import models.posts.Status;
import spark.Request;
import spark.Response;

public class DeleteContribution extends Status implements DeleteInterface {
    private HandleDB handleDB = new HandleDB();

    /**
     * 投稿の削除を受け付ける（Admin用）
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    public String requestDeleteWithoutKey(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), DeletePayload.class);
            if (!payload.isValidWithoutKey()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = handleDB.deleteContribution(payload.getId());
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response);

            return "OK";
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
    public String requestDelete(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), DeletePayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = handleDB.deleteContributionWithKey(payload.getId(), Encryption.getSaltedDeleteKey(payload.getDeleteKey(), payload.getUsername()));
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response);

            return "OK";
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
