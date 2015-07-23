package routes;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;
import java.util.HashMap;

/**
 * ルーティングを行うクラス
 */
public class ApplicationRoute implements ApplicationRouteInterface {
    private static ApplicationRoute applicationRoute = new ApplicationRoute();
    private HashMap<String, Object> model;

    private ApplicationRoute() {
        this.model = new HashMap<>();
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
    public void initServerConf() {
        port(9000); // ポート番号を設定
        initRoutes();
    }

    /**
     * ルーティングの設定を行う
     */
    private void initRoutes() {
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        get("/", ((request, response) -> {
            return getRoot(request, response);
        }), engine);

        get("/stop", ((request, response) -> {
            return getStop(request, response);
        }), engine);
    }

    /**
     * indexページを表示する
     * @param req リクエスト
     * @param res レスポンス
     * @return indexのModelAndView
     */
    @Override
    public ModelAndView getRoot(Request req, Response res) {
        model.put("msg", "hello");
        return new ModelAndView(model, "index.mustache.html");
    }


    /**
     * サーバを停止する
     * @param req リクエスト
     * @param res レスポンス
     * @return null
     */
    @Override
    public ModelAndView getStop(Request req, Response res) {
        stop();
        return null;
    }
}
