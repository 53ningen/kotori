package models.requests;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.posts.utils.Status;
import spark.Request;
import spark.Response;

public interface DBRequest extends Status {
    default String insert(Request request, Response response) {return "";}
    default String delete(Request request, Response response) {return "";}
    default String update(Request request, Response response) {return "";}

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
