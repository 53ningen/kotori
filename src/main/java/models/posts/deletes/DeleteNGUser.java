package models.posts.deletes;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.payloads.DeletePayload;
import models.posts.handles.HandleDB;
import models.posts.handles.HandleDBForNGUser;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

public class DeleteNGUser implements DeleteInterface {
    private static final DeleteNGUser deleteNGUser = new DeleteNGUser();

    public static DeleteNGUser getDeleteNGUser() {
        return deleteNGUser;
    }

    /**
     * NGユーザの削除を受け付ける（Admin用）
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    @Override
    public String requestDelete(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(request.body(), DeletePayload.class);
            if (!payload.isValidWithoutKey()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = HandleDB.ngUser().delete(payload.getId());
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response);

            return "ok";
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
