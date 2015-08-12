package models.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.contributions.Encryption;
import models.payloads.DeletePayload;
import models.payloads.HandlePayload;
import spark.Request;
import spark.Response;

public class DeleteContribution extends Status {
    private HandleDB handleDB = new HandleDB();

    /**
     * 投稿の削除を受け付ける（Admin用）
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    public String requestDeleteContribution(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), DeletePayload.class);
            if (!payload.isValidWithoutKey()) {
                return setBadRequest(response);
            }

            int result = handleDB.deleteContribution(payload.getId());
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response);

            return "OK";
        } catch (Exception e) {
            return setBadRequest(response);
        }
    }

    /**
     * 投稿の削除を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    public String requestDeleteContributionWithKey(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), DeletePayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response);
            }

            int result = handleDB.deleteContributionWithKey(payload.getId(), Encryption.getSaltedDeleteKey(payload.getDeleteKey(), payload.getUsername()));
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response);

            return "OK";
        } catch (Exception e) {
            return setBadRequest(response);
        }
    }
}
