package routes;

import databases.entities.User;
import logger.LogFile;
import models.utils.StatusCode;
import models.users.HandleUser;
import spark.Request;
import spark.Response;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static spark.Spark.*;

/**
 * ルーティングを行うクラス
 */
public class ApplicationRoute {
    private static final ApplicationRoute applicationRoute = new ApplicationRoute();
    private final LogFile logFile = LogFile.getLogFile();
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
     * ルーティングの設定を行う（アクセス前処理）
     */
    private void initRoutesBefore() {
        before("/", (req, res) -> {
            if (!getRequest.isLogin(req)) { // 未ログインであればログインページに飛ばす
                redirect(res, "/login");
            }
        });

        before("/my", (req, res) -> {
            if (!getRequest.isLogin(req)) {
                redirect(res, "/login");
            }
        });

        before("/search", (req, res) -> {
            if (!getRequest.isLogin(req)) { // 未ログインであればログインページに飛ばす
                redirect(res, "/login");
            }
        });

        before("/api/*", (req, res) -> {
            if (!getRequest.isLogin(req)) { // 未ログイン時のapi利用を許可しない
                halt(StatusCode.HTTP_FORBIDDEN.getStatusCode()); // 403 Forbiddenを返す
            }
        });

        before("/api/admin/*", (req, res) -> {
            if (!getRequest.isAdmin(req)) {
                halt(StatusCode.HTTP_FORBIDDEN.getStatusCode());
            }
        });

        before("/admin", (req, res) -> {
            if (!getRequest.isAdmin(req)) {
                halt(StatusCode.HTTP_FORBIDDEN.getStatusCode());
            }
        });

        before("/admin/*", (req, res) -> {
            if (!getRequest.isAdmin(req)) {
                halt(StatusCode.HTTP_FORBIDDEN.getStatusCode());
            }
        });
    }

    /**
     * ルーティングの設定を行う（getリクエスト）
     */
    private void initRoutesGet() {
        MustacheTemplateEngine engine = new MustacheTemplateEngine();

        get("/", ((req, res) -> getRequest.getPage(req, "index.mustache.html")), engine);

        get("/my", ((req, res) -> HandleUser.createUser(req).map(user -> getRequest.getMypage(req, user)).get()), engine);

        get("/login", ((req, res) -> getRequest.getLogin(req)), engine);

        get("/search", ((req, res) -> getRequest.getSearch(req)), engine);

        get("/admin", ((req, res) -> getRequest.getPage(req, "admin.mustache.html")), engine);

        get("/admin/log", ((req, res) -> getRequest.getLog(req)), engine);

        get("/admin/log/latest/:filename", (req, res) -> {
            List<String> resultList = logFile.getFileText(req.params("filename"));
            if (resultList.isEmpty()) halt(StatusCode.HTTP_NOTFOUND.getStatusCode());
            return resultList.toString();
        });

        get("/admin/log/:date/:filename", (req, res) -> {
            List<String> resultList = logFile.getFileText("history/" + req.params("date") + "/" + req.params("filename"));
            if (resultList.isEmpty()) halt(StatusCode.HTTP_NOTFOUND.getStatusCode());
            return resultList.toString();
        });

        get("/admin/ngword", ((req, res) -> getRequest.getAdminNGWord(req)), engine);

        get("/admin/nguser", ((req, res) -> getRequest.getAdminNGUser(req)), engine);
    }

    /**
     * ルーティングの設定を行う（postリクエスト）
     */
    private void initRoutesPost() {
        post("/api_login", ((req, res) -> {
            String result = postRequest.userRequest().select(req, res);
            if (result.equals("OK")) result = setAutoLogin(req, res);
            return result;
        }));

        post("/api_logout", ((req, res) -> postRequest.autoLoginRequest().delete(req, res)));

        post("/api_register", ((req, res) -> {
            String result = postRequest.userRequest().insert(req, res);
            if (result.equals("OK")) result = setAutoLogin(req, res);
            return result;
        }));

        post("/api/post", ((req, res) -> postRequest.contributionRequest().insert(req, res)));

        post("/api/delete", ((req, res) -> postRequest.contributionRequest().delete(req, res)));

        post("/api/admin/delete", ((req, res) -> postRequest.contributionRequest().deleteWithoutKey(req, res)));

        post("/api/admin/update", ((req, res) -> postRequest.contributionRequest().update(req, res)));

        post("/api/admin/delete_ngword", ((req, res) -> postRequest.ngWordRequest().delete(req, res)));

        post("/api/admin/delete_nguser", ((req, res) -> postRequest.ngUserRequest().delete(req, res)));

        post("/api/admin/insert_ngword", ((req, res) -> postRequest.ngWordRequest().insert(req, res)));

        post("/api/admin/insert_nguser", ((req, res) -> postRequest.ngUserRequest().insert(req, res)));
    }

    /**
     * 自動ログイン情報を登録する
     * @param request リクエスト
     * @param response レスポンス
     * @return 登録結果
     * @throws Exception
     */
    private String setAutoLogin(Request request, Response response) {
        try {
            User user = HandleUser.updateUser(postRequest.userRequest().createUser(request));
            return postRequest.autoLoginRequest().insert(user, response);
        } catch (IOException e) {
            halt(StatusCode.HTTP_INTERNAL_SERVER_ERROR.getStatusCode());
            return "";
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
