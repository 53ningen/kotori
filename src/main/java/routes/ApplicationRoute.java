package routes;

import static spark.Spark.*;

import databases.entities.Contribution;
import databases.entities.NGWord;
import models.contributions.HandleContribution;
import models.paginations.HandlePagination;
import models.posts.DeleteContribution;
import models.posts.PostContribution;
import models.posts.HandleDB;
import models.posts.UpdateContribution;
import models.requests.HandleRequest;
import models.responses.HandleResponse;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.List;

/**
 * ルーティングを行うクラス
 */
public class ApplicationRoute {
    private static ApplicationRoute applicationRoute = new ApplicationRoute();
    private PostContribution postContribution = new PostContribution();
    private UpdateContribution updateContribution = new UpdateContribution();
    private DeleteContribution deleteContribution = new DeleteContribution();
    private HandleDB handleDB = new HandleDB();
    private HandleContribution handleContribution = new HandleContribution();
    private HandlePagination handlePagination = new HandlePagination();
    private HandleRequest handleRequest = new HandleRequest();

    private ApplicationRoute() {
        initServerConf();
        initRoutes();
    }

    /**
     * インスタンスを取得する
     */
    public static ApplicationRoute getApplicationRoute() {
        return applicationRoute;
    }

    /**
     * サーバの設定を行う
     */
    private void initServerConf() {
        port(9000); // ポート番号を設定
        staticFileLocation("/templates"); // 静的ファイルのパスを設定
    }

    /**
     * ルーティングの設定を行う
     */
    private void initRoutes() {
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        get("/", (this::getRoot), engine);

        get("/search", (this::getSearch), engine);

        get("/admin", (this::getAdmin), engine);

        get("/admin/ng", (this::getAdminNG), engine);

        post("/api/post", (postContribution::requestPostContribution));

        post("/api/delete", (deleteContribution::requestDeleteContributionWithKey));

        post("/api/admin_delete", (deleteContribution::requestDeleteContribution));

        post("/api/admin_update", (updateContribution::requestUpdateContribution));
    }

    /**
     * indexページを表示する
     * @param req リクエスト
     * @param res レスポンス
     * @return ModelAndView
     */
    private ModelAndView getRoot(Request req, Response res) {
        handleRequest.updateHandleRequest(req);
        List<Contribution> contributions = handleDB.findContributionsWithLimit(handleRequest);
        return new ModelAndView(getResponseMap(req, contributions, ""), "index.mustache.html");
    }

    /**
     * 検索結果ページを表示する
     * @param req リクエスト
     * @param res レスポンス
     * @return ModelAndView
     */
    private ModelAndView getSearch(Request req, Response res) {
        handleRequest.updateHandleRequest(req);
        List<Contribution> contributions = handleDB.findContributionsByKeyword(handleRequest);
        return new ModelAndView(getResponseMap(req, contributions, "q"), "index.mustache.html");
    }

    /**
     * Adminページを表示する
     * @param req リクエスト
     * @param res レスポンス
     * @return ModelAndView
     */
    private ModelAndView getAdmin(Request req, Response res) {
        handleRequest.updateHandleRequest(req);
        List<Contribution> contributions = handleDB.findContributionsWithLimit(handleRequest);
        return new ModelAndView(getResponseMap(req, contributions, ""), "admin.mustache.html");
    }


    /**
     * NGワード管理ページを表示する
     * @param req リクエスト
     * @param res レスポンス
     * @return ModelAndView
     */
    private ModelAndView getAdminNG(Request req, Response res) {
        handleRequest.updateHandleRequest(req);
        List<NGWord> ngWords = handleDB.findAllNGWords();
        return new ModelAndView(getResponseMap(req, ngWords), "admin.ng.mustache.html");
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
                handlePagination.createPagination(handleDB, handleRequest),
                query
        ).getResponseMap();
    }

    /**
     * テンプレートエンジンに渡すレスポンスを生成する
     * @param request リクエスト
     * @param list Viewに渡すリスト
     * @param <T> リストの型
     * @return HashMap
     */
    private <T> HashMap<String, Object> getResponseMap(Request request, List<T> list) {
        return new HandleResponse(
                request,
                list
        ).getResponseMap();
    }
}
