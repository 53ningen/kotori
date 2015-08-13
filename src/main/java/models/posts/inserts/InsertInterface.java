package models.posts.inserts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;

public interface InsertInterface {
    String RESPONSE_TYPE_JSON = "application/json";
    String requestInsert(Request request, Response response);

    /**
     * 投稿情報をjson文字列に変換する
     * @param object 投稿情報
     * @return json文字列
     * @throws JsonProcessingException
     */
    default <T> String convertObjectToJson(T object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        return mapper.writeValueAsString(object);
    }
}
