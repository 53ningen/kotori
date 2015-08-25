package models.requests;

import models.posts.utils.Status;
import spark.Request;
import spark.Response;

public interface DBRequest extends Status {
    default String insert(Request request, Response response) {return "";}
    default String delete(Request request, Response response) {return "";}
}
