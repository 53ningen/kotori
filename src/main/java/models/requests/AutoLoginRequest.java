package models.requests;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.AutoLogin;
import models.payloads.HandlePayload;
import models.posts.handles.HandleDB;
import models.posts.handles.HandleDBForAutoLogin;
import models.posts.utils.CSRFToken;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.Optional;

public class AutoLoginRequest implements DBRequest {
    private final HandleDBForAutoLogin autoLogin = HandleDB.autoLogin();

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
            int result = autoLogin.insert(al);
            if (result < 1) {
                return setInternalServerError(response);
            }

            response.cookie("token", al.getToken(), 60 * 60 * 24 * 7);
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
            String token = request.cookie("token");
            if (token.isEmpty()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            Optional<AutoLogin> alOpt = autoLogin.selectByToken(token);
            Optional<Integer> result = alOpt.map(autoLogin::delete);
            if (result.orElse(-1) < 1) {
                return setInternalServerError(response);
            }

            return setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * ログイン情報をDBからID指定で一括削除する
     * @param request リクエスト
     * @param response レスポンス
     * @return 成功時はOK, 失敗時はエラー文を出力
     */
    public String deleteById(Request request, Response response) {
        try {
            String token = request.cookie("token");
            if (token.isEmpty()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            Optional<AutoLogin> alOpt = autoLogin.selectByToken(token);
            Optional<Integer> result = alOpt.map(al -> autoLogin.deleteByUserId(al.getUserid()));
            if (result.orElse(-1) < 1) {
                return setInternalServerError(response);
            }

            return  setOK(response);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }

    /**
     * ログイン中かどうかを確認する
     * @param request リクエスト
     * @return ログイン中ならtrueを返す
     */
    public boolean isLogin(Request request, Response response) {
        Optional<String> tokenOpt = Optional.ofNullable(request.cookie("token"));
        if (tokenOpt.isPresent()) {
            Optional<AutoLogin> alOpt = checkAuthToken(tokenOpt.get());
            if (alOpt.isPresent()) {
                AutoLogin al = alOpt.get();
                AutoLogin newAl = new AutoLogin(CSRFToken.getCSRFToken(), al.getUserid(), LocalDateTime.now().plusWeeks(1));
                response.cookie("token", newAl.getToken(), 60 * 60 * 24 * 7); // クッキーを更新
                autoLogin.delete(al); // 古いログイン情報を削除
                autoLogin.insert(newAl); // 新しいログイン情報を追加
                return true;
            } else {
                return false; // DBに存在しないか有効期限切れのため未ログイン
            }
        } else {
            return false; // クッキーを持っていないので未ログイン
        }
    }

    /**
     * トークンがDBに保管されているかを確認する
     * @param token トークン
     * @return OptionalなAutoLoginインスタンス
     */
    private Optional<AutoLogin> checkAuthToken(String token) {
        Optional<AutoLogin> alOpt = autoLogin.selectByToken(token);
        AutoLogin al;
        if (!alOpt.isPresent()) {
            return Optional.empty(); // DBに存在しない
        } else {
            al = alOpt.get();
            if (al.getExpire().isBefore(LocalDateTime.now())) { // 有効期限が過ぎている
                autoLogin.delete(al); // 古いログイン情報を削除
                return Optional.empty();
            }
        }
        return Optional.of(al); // トークンがDBに保管されており有効期限内である
    }
}
