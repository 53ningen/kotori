package routes;

import models.posts.deletes.Delete;
import models.posts.deletes.DeleteInterface;
import models.posts.inserts.Insert;
import models.posts.inserts.InsertInterface;
import models.posts.updates.Update;
import models.posts.updates.UpdateInterface;
import spark.Request;
import spark.Response;

public class PostRequest {
    private final Insert insert = new Insert();
    private final Delete delete = new Delete();
    private final Update update = new Update();

    /**
     * DBへの挿入を問い合わせる
     * @param request リクエスト
     * @param response レスポンス
     * @param insertInterface 実装クラス
     * @return 問い合わせ結果
     */
    protected String insert(Request request, Response response, InsertInterface insertInterface) {
        insert.setInsertInstance(insertInterface);
        return insert.requestInsert(request, response);
    }

    /**
     * DBへの更新を問い合わせる
     * @param request リクエスト
     * @param response レスポンス
     * @param updateInterface 実装クラス
     * @return 問い合わせ結果
     */
    protected String update(Request request, Response response, UpdateInterface updateInterface) {
        update.setUpdateInstance(updateInterface);
        return update.requestUpdate(request, response);
    }

    /**
     * DBへの削除を問い合わせる
     * @param request リクエスト
     * @param response レスポンス
     * @param deleteInterface 実装クラス
     * @return 問い合わせ結果
     */
    protected String delete(Request request, Response response, DeleteInterface deleteInterface) {
        delete.setDeleteInstance(deleteInterface);
        return delete.requestDelete(request, response);
    }

    protected String deleteWithoutKey(Request request, Response response, DeleteInterface deleteInterface) {
        delete.setDeleteInstance(deleteInterface);
        return delete.requestDeleteWithoutKey(request, response);
    }
}
