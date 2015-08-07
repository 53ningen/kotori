package models.requests;

import spark.Request;

public class HandleRequest {
    private final int DEFAULT_PAGE = 1;
    private final int DEFAULT_LIMIT = 10;
    private int page;
    private int limit;
    private String query;

    public HandleRequest() {
        page = DEFAULT_PAGE;
        limit = DEFAULT_LIMIT;
        query = "%";
    }

    /**
     * DBに渡すリクエストを更新する
     * @param req リクエスト
     */
    public void updateHandleRequest(Request req) {
        setShowPage(req);
        setShowLimit(req);
        setQuery(req);
    }

    /**
     * 表示するページ番号を設定する
     * @param req リクエスト
     */
    private void setShowPage(Request req) {
        try {
            int p = Integer.valueOf(getQueryMapValue(req, "s", "page"));
            page = p > 0 ? p : DEFAULT_PAGE;
        } catch (NumberFormatException | NullPointerException e) {
            page = DEFAULT_PAGE;
        }
    }

    /**
     * ページあたりの表示件数を設定する
     * @param req リクエスト
     */
    private void setShowLimit(Request req) {
        try {
            int l = Integer.valueOf(getQueryMapValue(req, "s", "limit"));
            limit = l > 0 ? l : DEFAULT_LIMIT;
        } catch (NumberFormatException | NullPointerException e) {
            limit = DEFAULT_LIMIT;
        }
    }

    /**
     * 検索クエリを設定する
     * @param req リクエスト
     */
    private void setQuery(Request req) {
        try {
            query = "%"+getQueryMapValue(req, "q", "keyword")+"%";
        } catch (NumberFormatException | NullPointerException e) {
            query = "%";
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
    private String getQueryMapValue(Request req, String query1, String query2) {
        String value = req.queryMap().get(query1, query2).value();
        if (value.equals("null")) return "-1";
        else return value;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public String getQuery() {
        return query;
    }
}
