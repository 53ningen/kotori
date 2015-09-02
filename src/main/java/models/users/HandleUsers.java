package models.users;

import databases.entities.User;
import models.posts.handles.HandleDB;
import spark.Request;

import java.util.Optional;

public class HandleUsers {
    private static final String AUTH_TOKEN = "auth_token";

    public static User createUser(Request request) {
        Optional<String> tokenOpt = Optional.ofNullable(request.cookie(AUTH_TOKEN));
        String userid = tokenOpt.map(token -> HandleDB.autoLogin().select(token)).orElse("notLogined");
        String username = HandleDB.user().selectUsername(userid);
        return new User(userid, username);
    }
}
