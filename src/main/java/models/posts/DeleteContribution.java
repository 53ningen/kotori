package models.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.contributions.Encryption;
import models.contributions.HandleContribution;
import models.payloads.DeletePayload;
import spark.Request;
import spark.Response;

public class DeleteContribution extends Status {
    private HandleDB handleDB = new HandleDB();
    private HandleContribution handleContribution = new HandleContribution();

    /**
     * 投稿の削除を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    public String requestDeleteContribution(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(handleContribution.unescapeUnicode(request.body()), DeletePayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response);
            }

            int result = handleDB.deleteContribution(payload.getId(), Encryption.getSaltedDeleteKey(payload.getDeleteKey(), payload.getUsername()));
            if (result < 1) {
                return setBadRequest(response);
            }

            setOK(response);

            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return setBadRequest(response);
        }
    }
}
