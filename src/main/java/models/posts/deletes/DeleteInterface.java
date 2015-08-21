package models.posts.deletes;

import models.posts.utils.Status;
import spark.Request;
import spark.Response;

public interface DeleteInterface extends Status {
    String requestDelete(Request request, Response response);
    default String requestDeleteWithoutKey(Request request, Response response) {return "";}
}
