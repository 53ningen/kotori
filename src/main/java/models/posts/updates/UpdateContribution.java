package models.posts.updates;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.payloads.HandlePayload;
import models.payloads.UpdatePayload;
import models.posts.handles.HandleDBForContribution;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

public class UpdateContribution implements UpdateInterface {
    private static final UpdateContribution updateContribution = new UpdateContribution();
    private HandleDBForContribution handleDBForContribution;

    private UpdateContribution() {
        handleDBForContribution = new HandleDBForContribution();
    }

    public static UpdateContribution getUpdateContribution() {
        return updateContribution;
    }

    /**
     * postによる投稿を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    @Override
    public String requestUpdate(Request request, Response response) {

        try {
            // UpdatePayloadを生成する
            UpdatePayload payload = new ObjectMapper().readValue(HandlePayload.unescapeUnicode(request.body()), UpdatePayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            // Payloadのパラメータが正しければDBを更新する
            int result = handleDBForContribution.update(payload);
            if (result < 1) {
                return setInternalServerError(response);
            }

            // ステータスコード200 OKを設定する
            setOK(response);

            return "OK";
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
