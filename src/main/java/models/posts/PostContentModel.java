package models.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParseException;
import spark.Request;
import spark.Response;

import java.io.IOException;

public class PostContentModel {
    private static final int HTTP_BAD_REQUEST = 400;
    private PostDB postDB;

    public PostContentModel() {
        postDB = new PostDB();
    }

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
            response.status(200);
            response.type("application/json");
            return id;
        } catch (JsonParseException jpe) {
            response.status(HTTP_BAD_REQUEST);
            return "";
        } catch (IOException e) {
            throw new RuntimeException("IOException from a readValue?");
        }
    }
}
