package routes;

import databases.entities.Contribution;
import databases.entities.NGInterface;
import databases.entities.NGUser;
import databases.entities.NGWord;
import models.contributions.HandleContribution;
import models.paginations.HandlePagination;
import models.posts.handles.HandleDBForContribution;
import models.posts.handles.HandleDBForNGUser;
import models.posts.handles.HandleDBForNGWord;
import models.posts.utils.DBSelectOptions;
import models.requests.HandleRequest;
import models.responses.HandleResponse;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;

public class GetRequest {
    private final HandleDBForContribution handleDBForContribution = new HandleDBForContribution();
    private final HandleDBForNGWord handleDBForNGWord = new HandleDBForNGWord();
    private final HandleDBForNGUser handleDBForNGUser = new HandleDBForNGUser();
    private final HandleContribution handleContribution = new HandleContribution();
    private final HandlePagination handlePagination = new HandlePagination();
    private final HandleRequest handleRequest = new HandleRequest();

    /**
     * 指定ページを表示する
     * @param req リクエスト
     * @param viewFile HTMLファイル名
     * @return ModelAndView
     */
    protected ModelAndView getPage(Request req, String viewFile) {
        handleRequest.updateHandleRequest(req);
        List<Contribution> contributions = handleDBForContribution.findWithLimit(handleRequest);
        return new ModelAndView(getResponseMap(req, contributions, ""), viewFile);
    }

    /**
     * 検索結果ページを表示する
     * @param req リクエスト
     * @param res レスポンス
     * @return ModelAndView
     */
    protected ModelAndView getSearch(Request req, Response res) {
        handleRequest.updateHandleRequest(req);
        List<Contribution> contributions = handleDBForContribution.findByKeyword(handleRequest);
        return new ModelAndView(getResponseMap(req, contributions, "q"), "index.mustache.html");
    }

    /**
     * NGワード管理ページを表示する
     * @param req リクエスト
     * @param res レスポンス
     * @return ModelAndView
     */
    protected ModelAndView getAdminNGWord(Request req, Response res) {
        handleRequest.updateHandleRequest(req);
        List<NGWord> ngWords = handleDBForNGWord.findAll();
        return new ModelAndView(getResponseMap(req, ngWords), "admin.ngword.mustache.html");
    }

    /**
     * NGユーザ管理ページを表示する
     * @param req リクエスト
     * @param res レスポンス
     * @return ModelAndView
     */
    protected ModelAndView getAdminNGUser(Request req, Response res) {
        handleRequest.updateHandleRequest(req);
        List<NGUser> ngUsers = handleDBForNGUser.findAll();
        return new ModelAndView(getResponseMap(req, ngUsers), "admin.nguser.mustache.html");
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
                handleContribution.addInformationContributions(contributions),
                handlePagination.createPagination(DBSelectOptions.getDBSelectOptions(), handleRequest),
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
}
