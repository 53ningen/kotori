package models.requests;

import spark.Request;

public class HandleRequest {
    private final int DEFAULT_LIMIT = 10;

    /**
     * ページあたりの表示件数を設定する
     * @param req リクエスト
     * @return 表示件数
     */
    public int setShowLimit(Request req) {
        try {
            int limit = req.queryMap().get("show", "limit").integerValue();
            return limit > 0 ? limit : DEFAULT_LIMIT;
        } catch (NumberFormatException | NullPointerException e) {
            return DEFAULT_LIMIT;
        }
    }
}
