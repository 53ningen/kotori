package models.posts.inserts;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.NGWord;
import models.payloads.HandlePayload;
import models.posts.utils.ErrorCode;
import models.posts.utils.Status;
import models.posts.handles.HandleDBForNGWord;
import spark.Request;
import spark.Response;

public class InsertNGWord extends Status implements InsertInterface {
    private static final InsertNGWord insertNGWord = new InsertNGWord();
    private HandleDBForNGWord handleDBForNGWord;

    private InsertNGWord() {
        handleDBForNGWord = new HandleDBForNGWord();
    }

    public static InsertNGWord getInsertNGWord() {
        return insertNGWord;
    }

    /**
     * postによるNGワード追加を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    @Override
    public String requestInsert(Request request, Response response) {

        try {
            NGWord ngWord = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), NGWord.class);
            if (!ngWord.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = handleDBForNGWord.insert(ngWord);
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response, RESPONSE_TYPE_JSON);

            return convertObjectToJson(ngWord);
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
