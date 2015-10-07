package models.users;

import databases.entities.User;
import spark.Request;

import java.util.Optional;

import static models.handles.HandleDB.autoLogin;
import static models.handles.HandleDB.user;

public class HandleUser {

    private static final String AUTH_TOKEN = "auth_token";
    private static final String USERID = "userid";
    private static final String USERNAME = "username";

    /**
     * OptionalなUserインスタンスを生成する
     *
     * @param request リクエスト
     * @return Optional型でラップされたUserインスタンス
     */
    public static Optional<User> createUser(final Request request) {
        final Optional<String> authToken = Optional.ofNullable(request.cookie(AUTH_TOKEN));
        final Optional<String> userId = authToken.flatMap(token -> autoLogin().select(token, USERID));
        final Optional<String> userName = authToken.flatMap(token -> autoLogin().select(token, USERNAME));
        return userId.flatMap(id -> userName.map(name -> new User(id, name)));
    }

    /**
     * UserインスタンスにUsernameの情報を追加する
     *
     * @param user Userインスタンス
     * @return 更新されたUserインスタンス
     */
    public static User updateUser(final User user) {
        if (user.getUsername() == null) {
            Optional.ofNullable(user().selectUsername(user.getUserid()))
                    .ifPresent(user::setUsername);
        }
        return user;
    }
}
