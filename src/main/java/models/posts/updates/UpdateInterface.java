package models.posts.updates;

import spark.Request;
import spark.Response;

public interface UpdateInterface {
    String requestUpdate(Request request, Response response);
}
