package routes;

import static spark.Spark.*;

import databases.entities.Contribution;
import models.contributions.HandleContribution;
import models.paginations.HandlePagination;
import models.posts.DeleteContribution;
import models.posts.PostContribution;
import models.posts.HandleDB;
import models.requests.HandleRequest;
import models.responses.HandleResponse;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.List;

/**
 * ルーティングを行うクラス
 */
public class ApplicationRoute {
    private static ApplicationRoute applicationRoute = new ApplicationRoute();
    private PostContribution postContribution = new PostContribution();
    private DeleteContribution deleteContribution = new DeleteContribution();
    private HandleDB handleDB = new HandleDB();
    private HandleContribution handleContribution = new HandleContribution();
    private HandlePagination handlePagination = new HandlePagination();
    private HandleRequest handleRequest = new HandleRequest();
    private HandleResponse handleResponse = new HandleResponse();

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

        post("/api/post", (postContribution::requestPostContribution));

        post("/api/delete", (deleteContribution::requestDeleteContributionWithKey));

        post("/api/admin_delete", (deleteContribution::requestDeleteContribution));
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
        setResponses(req, contributions, "");
        return new ModelAndView(handleResponse.getResponseMap(), "index.mustache.html");
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
        setResponses(req, contributions, "q");
        return new ModelAndView(handleResponse.getResponseMap(), "index.mustache.html");
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
        setResponses(req, contributions, "");
        return new ModelAndView(handleResponse.getResponseMap(), "admin.mustache.html");
    }

    /**
     * テンプレートエンジンに渡すレスポンスを生成する
     * @param request リクエスト
     * @param contributions 投稿情報
     * @param query urlで指定されたクエリ文字列
     */
    private void setResponses(Request request, List<Contribution> contributions, String query) {
        handleResponse.setResponseMap(
                request,
                handleContribution.addInformationContributions(contributions),
                handlePagination.createPagination(handleDB, handleRequest),
                query
        );
    }
}
