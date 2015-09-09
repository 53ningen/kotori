package routes;

import databases.entities.Contribution;
import databases.entities.NGUser;
import databases.entities.NGWord;
import databases.entities.User;
import logger.LogFile;
import models.paginations.HandlePagination;
import models.posts.handles.HandleDB;
import models.posts.utils.DBSelectOptions;
import models.requests.HandleRequest;
import models.responses.HandleResponse;
import models.users.HandleUser;
import org.seasar.doma.jdbc.SelectOptions;
import spark.ModelAndView;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GetRequest {
    private final LogFile logFile = LogFile.getLogFile();
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
     * ログページを表示する
     * @param req リクエスト
     * @return ModelAndView
     */
    protected ModelAndView getLog(Request req) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> latest = logFile.getLogFileNames("");
        List<String> old = logFile.getLogDirectoryNames();
        map.put("latest", latest);
        map.put("old", old);
        return new ModelAndView(getResponseMap(req, map), "admin.log.mustache.html");
    }

    /**
     * マイページを表示する
     * @param req リクエスト
     * @param user ユーザ情報
     * @return ModelAndView
     */
    protected ModelAndView getMypage(Request req, User user) {
        handleRequest.updateHandleRequest(req);
        List<Contribution> contributions = HandleDB.contribution().selectByUser(handleRequest, user);
        return new ModelAndView(getResponseMap(req, contributions, ""), "my.mustache.html");
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
        List<NGWord> ngWords = HandleDB.ngWord().select(SelectOptions.get());
        return new ModelAndView(getResponseMap(req, ngWords), "admin.ngword.mustache.html");
    }

    /**
     * NGユーザ管理ページを表示する
     * @param req リクエスト
     * @return ModelAndView
     */
    protected ModelAndView getAdminNGUser(Request req) {
        handleRequest.updateHandleRequest(req);
        List<NGUser> ngUsers = HandleDB.ngUser().selectAll();
        return new ModelAndView(getResponseMap(req, ngUsers), "admin.nguser.mustache.html");
    }

    /**
     * ログイン中かどうかを確認する
     * @param request リクエスト
     * @return ログイン中ならtrueを返す
     */
    protected boolean isLogin(Request request) {
        return Optional.ofNullable(request.cookie(AUTH_TOKEN))
                       .map(token -> HandleDB.autoLogin().existToken(token))
                       .orElse(false);
    }

    /**
     * Adminユーザかどうかを確認する
     * @param request リクエスト
     * @return Adminユーザであればtrueを返す
     */
    protected boolean isAdmin(Request request) {
        return Optional.ofNullable(request.cookie(AUTH_TOKEN))
                       .map(token -> HandleDB.autoLogin().select(token, "userid")
                               .map(userid -> HandleDB.user().selectAdminUser(userid))
                               .map(Optional::isPresent).orElse(false))
                       .orElse(false);
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
     * @param <T> 型クラス
     * @return HashMap
     */
    private <T> HashMap<String, Object> getResponseMap(Request request, List<T> list) {
        return new HandleResponse(
                request,
                list
        ).getResponseMap();
    }

    private <T> HashMap<String, Object> getResponseMap(Request request, Map<String, T> map) {
        return new HandleResponse(
                request,
                map
        ).getResponseMap();
    }

    private HashMap<String, Object> getResponseMap(Request request) {
        return new HandleResponse(request).getResponseMap();
    }
}
