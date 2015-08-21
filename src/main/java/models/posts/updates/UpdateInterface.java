package models.posts.updates;

import models.posts.utils.Status;
import spark.Request;
import spark.Response;

public interface UpdateInterface extends Status {
    String requestUpdate(Request request, Response response);
}
