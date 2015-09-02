package routes;

import databases.entities.*;
import models.paginations.HandlePagination;
import models.posts.handles.HandleDB;
import models.posts.utils.DBSelectOptions;
import models.requests.HandleRequest;
import models.responses.HandleResponse;
import models.users.HandleUser;
import spark.ModelAndView;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GetRequest {
    private final HandleRequest handleRequest = new HandleRequest();
    private final String AUTH_TOKEN = "auth_token";

    /**
     * 指定ページを表示する
     * @param req リクエスト
     * @param viewFile HTMLファイル名
     * @return ModelAndView
     */
    protected ModelAndView getPage(Request req, String viewFile) {
        handleRequest.updateHandleRequest(req);
        List<Contribution> contributions = HandleDB.contribution().findWithLimit(handleRequest);
        return new ModelAndView(getResponseMap(req, contributions, ""), viewFile);
    }

    /**
     * ログインページを表示する
     * @param req リクエスト
     * @return ModelAndView
     */
    protected ModelAndView getLogin(Request req) {
        return new ModelAndView(getResponseMap(req), "login.mustache.html");
    }

    /**
     * 検索結果ページを表示する
     * @param req リクエスト
     * @return ModelAndView
     */
    protected ModelAndView getSearch(Request req) {
        handleRequest.updateHandleRequest(req);
        List<Contribution> contributions = HandleDB.contribution().findByKeyword(handleRequest);
        return new ModelAndView(getResponseMap(req, contributions, "q"), "index.mustache.html");
    }

    /**
     * NGワード管理ページを表示する
     * @param req リクエスト
     * @return ModelAndView
     */
    protected ModelAndView getAdminNGWord(Request req) {
        handleRequest.updateHandleRequest(req);
        List<NGWord> ngWords = HandleDB.ngWord().findAll();
        return new ModelAndView(getResponseMap(req, ngWords), "admin.ngword.mustache.html");
    }

    /**
     * NGユーザ管理ページを表示する
     * @param req リクエスト
     * @return ModelAndView
     */
    protected ModelAndView getAdminNGUser(Request req) {
        handleRequest.updateHandleRequest(req);
        List<NGUser> ngUsers = HandleDB.ngUser().findAll();
        return new ModelAndView(getResponseMap(req, ngUsers), "admin.nguser.mustache.html");
    }

    /**
     * ログイン中かどうかを確認する
     * @param request リクエスト
     * @return ログイン中ならtrueを返す
     */
    protected boolean isLogin(Request request) {
        Optional<String> tokenOpt = Optional.ofNullable(request.cookie(AUTH_TOKEN));
        return tokenOpt.isPresent() && HandleDB.autoLogin().existToken(tokenOpt.get());
    }

    /**
     * テンプレートエンジンに渡すレスポンスを生成する
     * @param request リクエスト
     * @param contributions 投稿情報
     * @param query urlで指定されたクエリ文字列
     * @return HashMap
     */
    private HashMap<String, Object> getResponseMap(Request request, List<Contribution> contributions, String query) {
        return new HandleResponse(
                request,
                contributions,
                HandleUser.createUser(request),
                HandlePagination.createPagination(DBSelectOptions.getDBSelectOptions(), handleRequest),
                query
        ).getResponseMap();
    }

    /**
     * テンプレートエンジンに渡すレスポンスを生成する
     * @param request リクエスト
     * @param list Viewに渡すリスト
     * @param <T> NGInterfaceを実装する型クラス
     * @return HashMap
     */
    private <T extends NGInterface> HashMap<String, Object> getResponseMap(Request request, List<T> list) {
        return new HandleResponse(
                request,
                list
        ).getResponseMap();
    }

    private HashMap<String, Object> getResponseMap(Request request) {
        return new HandleResponse(request).getResponseMap();
    }
}
