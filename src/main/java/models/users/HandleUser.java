package models.users;

import databases.entities.User;
import spark.Request;

import java.util.HashMap;
import java.util.Optional;

import static models.posts.handles.HandleDB.autoLogin;
import static models.posts.handles.HandleDB.user;

public class HandleUser {
    private static final String AUTH_TOKEN = "auth_token";
    private static final String USERID = "userid";
    private static final String USERNAME = "username";
    private static final int USER_INFO_SIZE = 2;

    /**
     * OptionalなUserインスタンスを生成する
     * @param request リクエスト
     * @return Optional型でラップされたUserインスタンス
     */
    public static Optional<User> createUser(Request request) {
        HashMap<String, String> userMap = new HashMap<>();
        Optional<String> tokenOpt = Optional.ofNullable(request.cookie(AUTH_TOKEN));
        tokenOpt.ifPresent(token -> {
            autoLogin().select(token, USERID).ifPresent(id -> userMap.put(USERID, id));
            autoLogin().select(token, USERNAME).ifPresent(name -> userMap.put(USERNAME, name));
        });

        return userMap.size() == USER_INFO_SIZE ? Optional.of(new User(userMap.get(USERID), userMap.get(USERNAME))) : Optional.empty();
    }

    /**
     * UserインスタンスにUsernameの情報を追加する
     * @param user Userインスタンス
     * @return 更新されたUserインスタンス
     */
    public static User updateUser(User user) {
        if (user.getUsername() == null) {
            Optional<String> usernameOpt = Optional.ofNullable(user().selectUsername(user.getUserid()));
            usernameOpt.ifPresent(user::setUsername);
        }
        return user;
    }
}
