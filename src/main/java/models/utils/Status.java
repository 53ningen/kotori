package models.utils;

import spark.Response;

public interface Status {
    StatusCode HTTP_BAD_REQUEST = StatusCode.HTTP_BAD_REQUEST;
    StatusCode HTTP_UNAUTHORIZED = StatusCode.HTTP_UNAUTHORIZED;
    StatusCode HTTP_INTERNAL_SERVER_ERROR = StatusCode.HTTP_INTERNAL_SERVER_ERROR;
    StatusCode HTTP_OK = StatusCode.HTTP_OK;


    /**
     * 200 OK を設定する
     * @param response レスポンス
     */
    default String setOK(Response response) {
        response.status(HTTP_OK.getStatusCode());
        return "OK";
    }

    /**
     * ResponseTypeを指定して200 OKを設定する
     * @param response レスポンス
     * @param type レスポンスタイプ
     */
    default void setOK(Response response, ResponseType type) {
        response.status(HTTP_OK.getStatusCode());
        response.type(type.getResponseType());
    }

    /**
     * 400 BadRequest を設定する
     * @param response レスポンス
     * @param errorCode エラーコード
     * @return エラー文
     */
    default String setBadRequest(Response response, ErrorCode errorCode) {
        response.status(HTTP_BAD_REQUEST.getStatusCode());
        return errorCode.getErrorMsg();
    }

    /**
     * 401 Unauthorized を設定する
     * @param response レスポンス
     * @param errorCode エラーコード
     * @return エラー文
     */
    default String setUnauthorized(Response response, ErrorCode errorCode) {
        response.status(HTTP_UNAUTHORIZED.getStatusCode());
        return errorCode.getErrorMsg();
    }

    /**
     * 500 Internal Server Error を設定する
     * @param response レスポンス
     * @return 空文字
     */
    default String setInternalServerError(Response response) {
        response.status(HTTP_INTERNAL_SERVER_ERROR.getStatusCode());
        return "";
    }
}
