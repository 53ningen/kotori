package routes;

import static spark.Spark.*;

import databases.entities.Contribution;
import models.contributions.HandleContribution;
import models.paginations.HandlePagination;
import models.posts.PostContribution;
import models.posts.HandleDB;
import models.requests.HandleRequest;
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
    private HandleDB handleDB = new HandleDB();
    private HandleContribution handleContribution = new HandleContribution();
    private HandlePagination handlePagination = new HandlePagination();
    private HandleRequest handleRequest = new HandleRequest();
    private HashMap<String, Object> model = new HashMap<>();

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

        get("/stop", (this::stopServer));

        post("/post", (postContribution::requestPostContribution));

    }

    /**
     * indexページを表示する
     * @param req リクエスト
     * @param res レスポンス
     * @return indexのModelAndView
     */
    private ModelAndView getRoot(Request req, Response res) {
        List<Contribution> contributions = handleDB.findContributionsWithLimit(handleRequest.setShowPage(req), handleRequest.setShowLimit(req));
        model.put("contributions", handleContribution.addInformationContributions(contributions));
        model.put("pagination", handlePagination.createPagination(handleDB, handleRequest));
        return new ModelAndView(model, "index.mustache.html");
    }

    /**
     * サーバを停止する
     */
    private Object stopServer(Request req, Response res) {
        stop();
        return null;
    }
}
