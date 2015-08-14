package routes;

import static spark.Spark.*;

import databases.entities.Contribution;
import databases.entities.NGInterface;
import databases.entities.NGUser;
import databases.entities.NGWord;
import models.contributions.HandleContribution;
import models.paginations.HandlePagination;
import models.posts.deletes.DeleteContribution;
import models.posts.deletes.DeleteNGUser;
import models.posts.handles.HandleDBForNGUser;
import models.posts.handles.HandleDBForNGWord;
import models.posts.inserts.InsertContribution;
import models.posts.handles.HandleDBForContribution;
import models.posts.inserts.InsertNGUser;
import models.posts.inserts.InsertNGWord;
import models.posts.updates.UpdateContribution;
import models.posts.deletes.DeleteNGWord;
import models.posts.utils.DBSelectOptions;
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
    private InsertContribution insertContribution = new InsertContribution();
    private UpdateContribution updateContribution = new UpdateContribution();
    private DeleteContribution deleteContribution = new DeleteContribution();
    private InsertNGWord insertNGWord = new InsertNGWord();
    private InsertNGUser insertNGUser = new InsertNGUser();
    private DeleteNGWord deleteNGWord = new DeleteNGWord();
    private DeleteNGUser deleteNGUser = new DeleteNGUser();
    private HandleDBForContribution handleDBForContribution = new HandleDBForContribution();
    private HandleDBForNGWord handleDBForNGWord = new HandleDBForNGWord();
    private HandleDBForNGUser handleDBForNGUser = new HandleDBForNGUser();
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

        get("/", ((req, res) -> getPage(req, "index.mustache.html")), engine);

        get("/admin", ((req, res) -> getPage(req, "admin.mustache.html")), engine);

        get("/search", (this::getSearch), engine);

        get("/admin_ngword", (this::getAdminNGWord), engine);

        get("/admin_nguser", (this::getAdminNGUser), engine);

        post("/api/post", (insertContribution::requestInsert));

        post("/api/delete", (deleteContribution::requestDelete));

        post("/api/admin_delete", (deleteContribution::requestDeleteWithoutKey));

        post("/api/admin_update", (updateContribution::requestUpdate));

        post("/api/admin_delete_ngword", (deleteNGWord::requestDelete));

        post("/api/admin_insert_ngword", (insertNGWord::requestInsert));

        post("/api/admin_delete_nguser", (deleteNGUser::requestDelete));

        post("/api/admin_insert_nguser", (insertNGUser::requestInsert));
    }



    /**
     * 指定ページを表示する
     * @param req リクエスト
     * @param viewFile HTMLファイル名
     * @return ModelAndView
     */
    private ModelAndView getPage(Request req, String viewFile) {
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
    private ModelAndView getSearch(Request req, Response res) {
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
    private ModelAndView getAdminNGWord(Request req, Response res) {
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
    private ModelAndView getAdminNGUser(Request req, Response res) {
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
