package models.posts.utils;

public enum ResponseType {
    APPLICATION_JSON("application/json");

    private final String responseType;

    ResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getResponseType() {
        return responseType;
    }
}
