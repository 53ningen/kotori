package models.posts;

import spark.Response;

public class Status {
    private final int HTTP_BAD_REQUEST = 400;
    private final int HTTP_INTERNAL_SERVER_ERROR = 500;
    private final int HTTP_OK = 200;


    /**
     * 200 OK を設定する
     * @param response レスポンス
     */
    protected void setOK(Response response) {
        response.status(HTTP_OK);
    }

    /**
     * 400 BadRequest を設定する
     * @param response レスポンス
     * @return 空文字
     */
    protected String setBadRequest(Response response, String reason) {
        response.status(HTTP_BAD_REQUEST);
        return reason;
    }

    /**
     * 500 Internal Server Error を設定する
     * @param response レスポンス
     * @return 空文字
     */
    protected String setInternalServerError(Response response) {
        response.status(HTTP_INTERNAL_SERVER_ERROR);
        return "";
    }
}
