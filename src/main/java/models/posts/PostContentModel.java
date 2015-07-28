package models.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;

public class PostContentModel {
    private static final int HTTP_BAD_REQUEST = 400;
    private PostDB postDB = PostDB.getPostDB();

    /**
     * postによる投稿を受け付ける
     * @param request
     * @param response
     * @return 投稿ID
     */
    public String requestPostContent(Request request, Response response) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            PostPayload postPayload = mapper.readValue(request.body(), PostPayload.class);
            if (!postPayload.isValid()) {
                return sendBadRequest(response);
            }
            String id = postDB.createPost(postPayload);
            response.status(200);
            response.type("application/json");
            return id;
        } catch (Exception e) {
            return sendBadRequest(response);
        }
    }

    private String sendBadRequest(Response response) {
        response.status(HTTP_BAD_REQUEST);
        return "";
    }
}
