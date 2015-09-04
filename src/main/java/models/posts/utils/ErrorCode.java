package models.posts.utils;

public enum ErrorCode {
    PARAMETER_INVALID("パラメータが不正のため投稿できませんでした"),
    REGISTERED_ID("該当のユーザIDは既に登録されています"),
    LOGIN_FAILED("ユーザIDまたはパスワードが間違っています"),
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
