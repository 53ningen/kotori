package models.requests;

import spark.Request;

public class HandleRequest {
    private final int DEFAULT_PAGE = 0;
    private final int DEFAULT_LIMIT = 10;

    /**
     * 表示するページ番号を設定する
     * @param req リクエスト
     * @return 表示ページ番号
     */
    public int setShowPage(Request req) {
        try {
            int page = getQueryMapValue(req, "show", "page");
            return page > 0 ? page : DEFAULT_PAGE;
        } catch (NumberFormatException | NullPointerException e) {
            return DEFAULT_PAGE;
        }
    }

    /**
     * ページあたりの表示件数を設定する
     * @param req リクエスト
     * @return 表示件数
     */
    public int setShowLimit(Request req) {
        try {
            int limit = getQueryMapValue(req, "show", "limit");
            return limit > 0 ? limit : DEFAULT_LIMIT;
        } catch (NumberFormatException | NullPointerException e) {
            return DEFAULT_LIMIT;
        }
    }

    /**
     * クエリマップから設定された値を返す
     * @param req リクエスト
     * @param query1 クエリキー
     * @param query2 クエリキー
     * @return 設定値（設定されていない場合は-1）
     * @throws NumberFormatException
     * @throws NullPointerException
     */
    private int getQueryMapValue(Request req, String query1, String query2) throws NumberFormatException, NullPointerException {
        String value = req.queryMap().get(query1, query2).value();
        if (value.equals("null")) return -1;
        else return Integer.valueOf(value);
    }
}
