package models.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.User;
import models.payloads.HandlePayload;
import models.handles.HandleDB;
import models.utils.ErrorCode;
import org.seasar.doma.jdbc.UniqueConstraintException;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Optional;

public class UserRequest implements DBRequest {

    /**
     * postによるユーザ追加を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     * */
    @Override
    public String insert(Request request, Response response) {

        try {
            User user = createUser(request);
            if (!user.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            } else if (!HandlePayload.isValidUser(HandleDB.ngUser().selectAll(), Optional.of(user))) {
                return setBadRequest(response, ErrorCode.REGISTERED_ID);
            }

            int result = HandleDB.user().insert(user);
            if (result < 1) {
                return setInternalServerError(response);
            }

            return setOK(response);
        } catch (UniqueConstraintException e) {
            return setBadRequest(response, ErrorCode.REGISTERED_ID);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * ユーザのログイン処理を行う
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    @Override
    public String select(Request request, Response response) {

        try {
            User user = createUser(request);
            if (!user.isValidLogin()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            Optional<User> userOpt = HandleDB.user().select(user);
            if (!userOpt.isPresent()) {
                return setUnauthorized(response, ErrorCode.LOGIN_FAILED);
            }

            return setOK(response);
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
            User user = createUser(request);
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

    /**
     * HTTPリクエストのbodyをUserインスタンスに変換する
     * @param request リクエスト
     * @return Userインスタンス
     * @throws IOException
     */
    public User createUser(Request request) throws IOException {
        return new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), User.class);
    }
}
