package models.posts.inserts;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.NGUser;
import databases.entities.User;
import models.payloads.HandlePayload;
import models.posts.handles.HandleDB;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

public class InsertUser implements InsertInterface {
    private static final InsertUser insertUser = new InsertUser();

    public static InsertUser getInsertUser() {
        return insertUser;
    }

    /**
     * postによるユーザ追加を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    @Override
    public String requestInsert(Request request, Response response) {

        try {
            User user = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), User.class);
            if (!user.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = HandleDB.user().insert(user);
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response, RESPONSE_TYPE_JSON);

            return convertObjectToJson(user);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}

