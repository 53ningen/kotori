package models.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.Contribution;
import spark.Request;
import spark.Response;

import java.util.Optional;

public class PostContentModel {
    private static final int HTTP_BAD_REQUEST = 400;
    private PostDB postDB = PostDB.getPostDB();

    /**
     * postによる投稿を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    public int requestPostContent(Request request, Response response) {

        try {
            // postPayloadを生成する
            PostPayload postPayload = new ObjectMapper().readValue(request.body(), PostPayload.class);
            if (!postPayload.isValid()) {
                return sendBadRequest(response);
            }

            // OptionalなContributionを生成する
            Optional<Contribution> contributionOpt = postDB.createContribution(postPayload);

            // ContributionがNotNullならばDBに挿入する
            int result = postDB.insertContribution(contributionOpt.get());
            if (result < 1) {
                return sendBadRequest(response);
            }

            // ステータスコード200 OKを設定する
            response.status(200);
            response.type("application/json");
            return result;
        } catch (Exception e) {
            // ステータスコード400 BadRequestを設定する
            return sendBadRequest(response);
        }
    }

    /**
     * 400 BadRequest を設定する
     * @param response レスポンス
     * @return -1
     */
    private int sendBadRequest(Response response) {
        response.status(HTTP_BAD_REQUEST);
        return -1;
    }
}
