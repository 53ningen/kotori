package models.users;

import databases.entities.User;
import spark.Request;

import java.util.Optional;

import static models.posts.handles.HandleDB.autoLogin;
import static models.posts.handles.HandleDB.user;

public class HandleUser {
    private static final String AUTH_TOKEN = "auth_token";

    /**
     * OptionalなUserインスタンスを生成する
     * @param request リクエスト
     * @return Optional型でラップされたUserインスタンス
     */
    public static Optional<User> createUser(Request request) {
        Optional<String> useridOpt = Optional.ofNullable(request.cookie(AUTH_TOKEN))
                                             .map(token -> autoLogin().select(token));
        Optional<String> usernameOpt = useridOpt.map(userid -> user().selectUsername(userid));

        return usernameOpt.map(username -> new User(useridOpt.get(), username));
    }
}
