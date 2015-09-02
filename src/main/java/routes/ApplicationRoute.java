package routes;

import databases.entities.User;
import models.posts.utils.StatusCode;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.IOException;

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
            if (!getRequest.isLogin(req)) { // 未ログインであればログインページに飛ばす
                redirect(res, "/login");
            }
        });

        before("/search", (req, res) -> {
            if (!getRequest.isLogin(req)) {
                redirect(res, "/login");
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
        post("/api_login", ((req, res) -> {
            String result = postRequest.userRequest().select(req, res);
            if (result.equals("OK")) setAutoLogin(req, res);
            return result;
        }));

        post("/api_register", ((req, res) -> {
            String result = postRequest.userRequest().insert(req, res);
            if (result.equals("OK")) setAutoLogin(req, res);
            return result;
        }));

        post("/api/logout", ((req, res) -> postRequest.autoLoginRequest().delete(req, res)));

        post("/api/post", ((req, res) -> postRequest.contributionRequest().insert(req, res)));

        post("/api/delete", ((req, res) -> postRequest.contributionRequest().delete(req, res)));

        post("/api/admin_delete", ((req, res) -> postRequest.contributionRequest().deleteWithoutKey(req, res)));

        post("/api/admin_update", ((req, res) -> postRequest.contributionRequest().update(req, res)));

        post("/api/admin_delete_ngword", ((req, res) -> postRequest.ngWordRequest().delete(req, res)));

        post("/api/admin_delete_nguser", ((req, res) -> postRequest.ngUserRequest().delete(req, res)));

        post("/api/admin_insert_ngword", ((req, res) -> postRequest.ngWordRequest().insert(req, res)));

        post("/api/admin_insert_nguser", ((req, res) -> postRequest.ngUserRequest().insert(req, res)));
    }

    /**
     * 自動ログイン情報を登録する
     * @param request リクエスト
     * @param response レスポンス
     * @throws Exception
     */
    private void setAutoLogin(Request request, Response response) {
        try {
            User user = postRequest.userRequest().createUser(request);
            postRequest.autoLoginRequest().insert(user.getUserid(), response);
        } catch (IOException e) {
            halt(StatusCode.HTTP_INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

    /**
     * リダイレクト処理を行う
     * @param response レスポンス
     * @param path リダイレクト先パス
     */
    private void redirect(Response response, String path) {
        response.redirect(path);
        halt();
    }
}
