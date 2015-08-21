package routes;

import static spark.Spark.*;

import models.posts.deletes.*;
import models.posts.inserts.*;
import models.posts.updates.UpdateContribution;
import spark.template.mustache.MustacheTemplateEngine;

/**
 * ルーティングを行うクラス
 */
public class ApplicationRoute {
    private static final ApplicationRoute applicationRoute = new ApplicationRoute();
    private final PostRequest postRequest = new PostRequest();
    private final GetRequest getRequest = new GetRequest();

    private ApplicationRoute() {
        initServerConf();
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
     * ルーティングの設定を行う
     */
    private void initRoutesGet() {
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        get("/", ((req, res) -> getRequest.getPage(req, "index.mustache.html")), engine);

        get("/admin", ((req, res) -> getRequest.getPage(req, "admin.mustache.html")), engine);

        get("/search", ((req, res) -> getRequest.getSearch(req)), engine);

        get("/admin_ngword", ((req, res) -> getRequest.getAdminNGWord(req)), engine);

        get("/admin_nguser", ((req, res) -> getRequest.getAdminNGUser(req)), engine);
    }

    private void initRoutesPost() {
        post("/api/post", ((req, res) -> postRequest.insert(req, res, InsertContribution.getInsertContribution())));

        post("/api/delete", ((req, res) -> postRequest.delete(req, res, DeleteContribution.getDeleteContribution())));

        post("/api/admin_delete", ((req, res) -> postRequest.deleteWithoutKey(req, res, DeleteContribution.getDeleteContribution())));

        post("/api/admin_update", ((req, res) -> postRequest.update(req, res, UpdateContribution.getUpdateContribution())));

        post("/api/admin_delete_ngword", ((req, res) -> postRequest.delete(req, res, DeleteNGWord.getDeleteNGWord())));

        post("/api/admin_delete_nguser", ((req, res) -> postRequest.delete(req, res, DeleteNGUser.getDeleteNGUser())));

        post("/api/admin_insert_ngword", ((req, res) -> postRequest.insert(req, res, InsertNGWord.getInsertNGWord())));

        post("/api/admin_insert_nguser", ((req, res) -> postRequest.insert(req, res, InsertNGUser.getInsertNGUser())));
    }
}
