package models.posts.utils;

public enum ErrorCode {
    PARAMETER_INVALID("パラメータが不正のため投稿できませんでした"),
    NGWORD_CONTAINS("NGワードが含まれているため投稿できませんでした"),
    NGUSER("投稿が禁止されています");

    private final String errorMsg;

    ErrorCode(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
