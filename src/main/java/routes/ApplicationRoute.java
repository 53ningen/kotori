package routes;

import spark.template.mustache.MustacheTemplateEngine;

import static spark.Spark.*;

/**
 * ルーティングを行うクラス
 */
public class ApplicationRoute {
    private static final ApplicationRoute applicationRoute = new ApplicationRoute();
    private final PostRequest postRequest = new PostRequest();
    private final GetRequest getRequest = new GetRequest();

    private ApplicationRoute() {
        initServerConf();
        initRoutesBefore();
        initRoutesGet();
        initRoutesPost();
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
     * ルーティングの設定を行う（ログインの確認）
     */
    private void initRoutesBefore() {
        before("/", (req, res) -> {
            if (!getRequest.isLogin(req))  { // 未ログインであればログインページに飛ばす
                res.redirect("/login");
                halt();
            }
        });
    }

    /**
     * ルーティングの設定を行う（getリクエスト）
     */
    private void initRoutesGet() {
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        get("/", ((req, res) -> getRequest.getPage(req, "index.mustache.html")), engine);

        get("/login", ((req, res) -> getRequest.getLogin(req)), engine);

        get("/admin", ((req, res) -> getRequest.getPage(req, "admin.mustache.html")), engine);

        get("/search", ((req, res) -> getRequest.getSearch(req)), engine);

        get("/admin_ngword", ((req, res) -> getRequest.getAdminNGWord(req)), engine);

        get("/admin_nguser", ((req, res) -> getRequest.getAdminNGUser(req)), engine);
    }

    /**
     * ルーティングの設定を行う（postリクエスト）
     */
    private void initRoutesPost() {
        post("/api/login", ((req, res) -> postRequest.autoLoginRequest().insert(req, res)));

        post("/api/logout", ((req, res) -> postRequest.autoLoginRequest().delete(req, res)));

        post("/api/post", ((req, res) -> postRequest.contributionRequest().insert(req, res)));

        post("/api/delete", ((req, res) -> postRequest.contributionRequest().delete(req, res)));

        post("/api/user/insert", ((req, res) -> postRequest.userRequest().insert(req, res)));

        post("/api/user/delete", ((req, res) -> postRequest.userRequest().delete(req, res)));

        post("/api/admin_delete", ((req, res) -> postRequest.contributionRequest().deleteWithoutKey(req, res)));

        post("/api/admin_update", ((req, res) -> postRequest.contributionRequest().update(req, res)));

        post("/api/admin_delete_ngword", ((req, res) -> postRequest.ngWordRequest().delete(req, res)));

        post("/api/admin_delete_nguser", ((req, res) -> postRequest.ngUserRequest().delete(req, res)));

        post("/api/admin_insert_ngword", ((req, res) -> postRequest.ngWordRequest().insert(req, res)));

        post("/api/admin_insert_nguser", ((req, res) -> postRequest.ngUserRequest().insert(req, res)));
    }
}
