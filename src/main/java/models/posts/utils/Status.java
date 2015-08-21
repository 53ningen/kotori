package models.posts.utils;

import spark.Response;

public class Status {
    private final StatusCode HTTP_BAD_REQUEST = StatusCode.HTTP_BAD_REQUEST;
    private final StatusCode HTTP_INTERNAL_SERVER_ERROR = StatusCode.HTTP_INTERNAL_SERVER_ERROR;
    private final StatusCode HTTP_OK = StatusCode.HTTP_OK;


    /**
     * 200 OK を設定する
     * @param response レスポンス
     */
    protected void setOK(Response response) {
        response.status(HTTP_OK.getStatusCode());
    }

    /**
     * ResponseTypeを指定して200 OKを設定する
     * @param response レスポンス
     * @param type レスポンスタイプ
     */
    protected void setOK(Response response, String type) {
        response.status(HTTP_OK.getStatusCode());
        response.type(type);
    }

    /**
     * 400 BadRequest を設定する
     * @param response レスポンス
     * @return 空文字
     */
    protected String setBadRequest(Response response, ErrorCode errorCode) {
        response.status(HTTP_BAD_REQUEST.getStatusCode());
        return errorCode.getErrorMsg();
    }

    /**
     * 500 Internal Server Error を設定する
     * @param response レスポンス
     * @return 空文字
     */
    protected String setInternalServerError(Response response) {
        response.status(HTTP_INTERNAL_SERVER_ERROR.getStatusCode());
        return "";
    }
}
