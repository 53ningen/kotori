package models.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.contributions.HandleContribution;
import models.payloads.UpdatePayload;
import spark.Request;
import spark.Response;

public class UpdateContribution extends Status {
    private HandleDB handleDB = new HandleDB();
    private HandleContribution handleContribution = new HandleContribution();

    /**
     * postによる投稿を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return 投稿処理数
     */
    public String requestUpdateContribution(Request request, Response response) {

        try {
            // UpdatePayloadを生成する
            UpdatePayload payload = new ObjectMapper().readValue(handleContribution.unescapeUnicode(request.body()), UpdatePayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response);
            }

            // Payloadのパラメータが正しければDBを更新する
            int result = handleDB.updateContribution(payload);
            if (result < 1) {
                return setInternalServerError(response);
            }

            // ステータスコード200 OKを設定する
            setOK(response);

            return "OK";
        } catch (Exception e) {
            // ステータスコード400 BadRequestを設定する
            return setBadRequest(response);
        }
    }
}
