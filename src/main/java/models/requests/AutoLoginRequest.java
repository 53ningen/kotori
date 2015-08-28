package models.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.AutoLogin;
import models.payloads.HandlePayload;
import models.posts.handles.HandleDB;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class AutoLoginRequest implements DBRequest {
    private final String AUTH_TOKEN = "auth_token";

    /**
     * ログイン情報をDBに追加する
     * @param request リクエスト
     * @param response レスポンス
     * @return 成功時はOK, 失敗時はエラー文を出力
     */
    @Override
    public String insert(Request request, Response response) {
        try {
            AutoLogin al = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), AutoLogin.class);

            Long result = HandleDB.autoLogin().insert(al);
            if (result < 1) {
                setInternalServerError(response);
            }

            response.cookie(AUTH_TOKEN, al.getToken(), 60 * 60 * 24 * 7);
            return setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
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

            return setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * ログイン中かどうかを確認する
     * @param request リクエスト
     * @return ログイン中ならtrueを返す
     */
    public boolean isLogin(Request request) {
        Optional<String> tokenOpt = Optional.ofNullable(request.cookie(AUTH_TOKEN));
        return tokenOpt.isPresent() && HandleDB.autoLogin().existToken(tokenOpt.get());
    }
}
