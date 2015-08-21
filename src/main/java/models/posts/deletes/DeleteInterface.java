package models.posts.deletes;

import spark.Request;
import spark.Response;

public interface DeleteInterface {
    String requestDelete(Request request, Response response);
    default String requestDeleteWithoutKey(Request request, Response response) {return "";}
}
