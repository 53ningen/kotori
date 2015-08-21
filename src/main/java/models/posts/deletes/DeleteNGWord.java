package models.posts.deletes;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.payloads.DeletePayload;
import models.posts.handles.HandleDB;
import models.posts.handles.HandleDBForNGWord;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

public class DeleteNGWord implements DeleteInterface {
    private static final DeleteNGWord deleteNGWord = new DeleteNGWord();

    public static DeleteNGWord getDeleteNGWord() {
        return deleteNGWord;
    }

    /**
     * NGワードの削除を受け付ける（Admin用）
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

            int result = HandleDB.ngWord().delete(payload.getId());
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
