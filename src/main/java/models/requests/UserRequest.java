package models.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.User;
import models.payloads.HandlePayload;
import models.posts.handles.HandleDB;
import models.posts.utils.ErrorCode;
import models.posts.utils.ResponseType;
import org.seasar.doma.jdbc.UniqueConstraintException;
import spark.Request;
import spark.Response;

public class UserRequest implements DBRequest {

    /**
     * postによるユーザ追加を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    @Override
    public String insert(Request request, Response response) {

        try {
            User user = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), User.class);
            if (!user.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = HandleDB.user().insert(user);
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response, ResponseType.APPLICATION_JSON);

            return convertObjectToJson(user);
        } catch (UniqueConstraintException e) {
            return setBadRequest(response, ErrorCode.REGISTERED_ID);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * ユーザの削除を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    @Override
    public String delete(Request request, Response response) {

        try {
            User user = new ObjectMapper().readValue(request.body(), User.class);
            if (!user.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = HandleDB.user().delete(user);
            if (result < 1) {
                return setInternalServerError(response);
            }

            return setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
