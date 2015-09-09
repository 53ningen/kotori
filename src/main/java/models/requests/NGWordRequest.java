package models.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.NGWord;
import models.payloads.DeletePayload;
import models.payloads.HandlePayload;
import models.posts.handles.HandleDB;
import models.posts.utils.ErrorCode;
import models.posts.utils.ResponseType;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class NGWordRequest implements DBRequest {

    /**
     * postによるNGワード追加を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    @Override
    public String insert(Request request, Response response) {

        try {
            NGWord ngWord = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), NGWord.class);
            if (!ngWord.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            Optional<NGWord> ngWordOpt = Optional.ofNullable(HandleDB.ngWord().insert(ngWord))
                                                 .map(words -> words.get(0));

            if (!ngWordOpt.isPresent()) {
                return setInternalServerError(response);
            }

            setOK(response, ResponseType.APPLICATION_JSON);

            return convertObjectToJson(ngWordOpt.get());
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }


    /**
     * NGワードの削除を受け付ける（Admin用）
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    @Override
    public String delete(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(request.body(), DeletePayload.class);
            if (!payload.isValidWithoutKey()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = HandleDB.ngWord().delete(payload.getId());
            if (result < 1) {
                return setInternalServerError(response);
            }

            return setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
