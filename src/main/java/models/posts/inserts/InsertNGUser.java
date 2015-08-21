package models.posts.inserts;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.NGUser;
import models.payloads.HandlePayload;
import models.posts.handles.HandleDBForNGUser;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

public class InsertNGUser implements InsertInterface {
    private static final InsertNGUser insertNGUser = new InsertNGUser();
    private HandleDBForNGUser handleDBForNGUser;

    private InsertNGUser() {
        handleDBForNGUser = new HandleDBForNGUser();
    }

    public static InsertNGUser getInsertNGUser() {
        return insertNGUser;
    }

    /**
     * postによるNGユーザ追加を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    @Override
    public String requestInsert(Request request, Response response) {

        try {
            NGUser ngUser = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), NGUser.class);
            if (!ngUser.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = handleDBForNGUser.insert(ngUser);
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response, RESPONSE_TYPE_JSON);

            return convertObjectToJson(ngUser);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
