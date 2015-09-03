package models.requests;

import databases.entities.AutoLogin;
import databases.entities.User;
import models.posts.handles.HandleDB;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class AutoLoginRequest implements DBRequest {
    private final String AUTH_TOKEN = "auth_token";

    /**
     * ログイン情報をuserインスタンスを基に追加する
     * @param user ユーザインスタンス
     * @param response レスポンス
     * @return 成功時はOK, 失敗時はInternalServerErrorを返す
     */
    public String insert(User user, Response response) {
        AutoLogin al = new AutoLogin(user.getUserid(), user.getUsername());
        return insertAutoLoginInfo(al, response);
    }

    /**
     * ログイン情報をDBから削除する
     * @param request リクエスト
     * @param response レスポンス
     * @return 成功時はOK, 失敗時はエラー文を出力
     */
    @Override
    public String delete(Request request, Response response) {
        try {
            Optional<String> tokenOpt = Optional.ofNullable(request.cookie(AUTH_TOKEN));
            if (!tokenOpt.isPresent()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            Optional<Long> result = Optional.ofNullable(HandleDB.autoLogin().delete(tokenOpt.get()));
            if (result.orElse(-1L) < 1) {
                return setInternalServerError(response);
            }

            response.removeCookie(AUTH_TOKEN);
            return setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }


    /**
     * ログイン情報をDBに追加する
     * @param al AutoLoginインスタンス
     * @param response レスポンス
     * @return 成功時はOK, 失敗時はInternalServerErrorを返す
     */
    private String insertAutoLoginInfo(AutoLogin al, Response response) {
        Long result = HandleDB.autoLogin().insert(al);
        if (result < 1) {
            return setInternalServerError(response);
        }

        response.cookie(AUTH_TOKEN, al.getToken(), 60 * 60 * 24 * 7);
        return setOK(response);
    }
}
