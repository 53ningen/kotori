package models.posts.deletes;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.contributions.Encryption;
import models.payloads.DeletePayload;
import models.payloads.HandlePayload;
import models.posts.handles.HandleDB;
import models.posts.handles.HandleDBForContribution;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

public class DeleteContribution implements DeleteInterface {
    private static final DeleteContribution deleteContribution = new DeleteContribution();

    public static DeleteContribution getDeleteContribution() {
        return deleteContribution;
    }

    /**
     * 投稿の削除を受け付ける（Admin用）
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    @Override
    public String requestDeleteWithoutKey(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), DeletePayload.class);
            if (!payload.isValidWithoutKey()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = HandleDB.contribution().delete(payload.getId());
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

            int result = HandleDB.contribution().deleteWithKey(payload.getId(), Encryption.getSaltedDeleteKey(payload.getDeleteKey(), payload.getUsername()));
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
