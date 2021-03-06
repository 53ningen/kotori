package models.utils;

public enum StatusCode {
    HTTP_BAD_REQUEST(400),
    HTTP_UNAUTHORIZED(401),
    HTTP_FORBIDDEN(403),
    HTTP_NOTFOUND(404),
    HTTP_INTERNAL_SERVER_ERROR(500),
    HTTP_OK(200);

    private final int statusCode;

    StatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}