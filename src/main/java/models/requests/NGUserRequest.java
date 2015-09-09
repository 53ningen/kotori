package models.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.NGUser;
import models.payloads.DeletePayload;
import models.payloads.HandlePayload;
import models.posts.handles.HandleDB;
import models.posts.utils.ErrorCode;
import models.posts.utils.ResponseType;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class NGUserRequest implements DBRequest {

    /**
     * postによるNGユーザ追加を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    @Override
    public String insert(Request request, Response response) {

        try {
            NGUser ngUser = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), NGUser.class);
            if (!ngUser.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            Optional<NGUser> ngUserOpt = Optional.ofNullable(HandleDB.ngUser().insert(ngUser))
                                                 .map(users -> users.get(0));
            if (!ngUserOpt.isPresent()) {
                return setInternalServerError(response);
            }

            setOK(response, ResponseType.APPLICATION_JSON);

            return convertObjectToJson(ngUserOpt.get());
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * NGユーザの削除を受け付ける（Admin用）
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

            int result = HandleDB.ngUser().delete(payload.getId());
            if (result < 1) {
                return setInternalServerError(response);
            }

            return setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
