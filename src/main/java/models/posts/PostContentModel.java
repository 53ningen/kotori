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
                response.status(HTTP_BAD_REQUEST);
                return "";
            }
            String id = postDB.createPost(postPayload);
            postPayload.setId(id);
            response.status(200);
            response.type("application/json");
            return id;
        } catch (Exception e) {
            response.status(HTTP_BAD_REQUEST);
            return "";
        }
    }
}
