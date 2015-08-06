package models.requests;

import spark.Request;

public class HandleRequest {
    private final int DEFAULT_PAGE = 1;
    private final int DEFAULT_LIMIT = 10;
    private int showPage;
    private int showLimit;
    private String query;

    public void setRequest(Request req) {
        setShowPage(req);
        setShowLimit(req);
    }

    public void setRequestWithQuery(Request req) {
        setShowPage(req);
        setShowLimit(req);
        setQuery(req);
    }

    /**
     * 表示するページ番号を設定する
     * @param req リクエスト
     * @return 表示ページ番号
     */
    private void setShowPage(Request req) {
        try {
            int page = Integer.valueOf(getQueryMapValue(req, "show", "page"));
            showPage = page > 0 ? page : DEFAULT_PAGE;
        } catch (NumberFormatException | NullPointerException e) {
            showPage = DEFAULT_PAGE;
        }
    }

    public int getShowPage() {
        return showPage;
    }

    /**
     * ページあたりの表示件数を設定する
     * @param req リクエスト
     * @return 表示件数
     */
    private void setShowLimit(Request req) {
        try {
            int limit = Integer.valueOf(getQueryMapValue(req, "show", "limit"));
            showLimit = limit > 0 ? limit : DEFAULT_LIMIT;
        } catch (NumberFormatException | NullPointerException e) {
            showLimit = DEFAULT_LIMIT;
        }
    }

    public int getShowLimit() {
        return showLimit;
    }

    private void setQuery(Request req) {
        try {
            query = "%"+getQueryMapValue(req, "q", "title")+"%";
        } catch (NumberFormatException | NullPointerException e) {
            query = "%";
        }
    }

    public String getQuery() {
        return query;
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
}
