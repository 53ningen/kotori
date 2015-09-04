package bulletinBoard;

import routes.ApplicationRoute;

public class Global {

    public static void main(String args[]) {

        ApplicationRoute.getApplicationRoute();
        RedisServer.getRedisServer();

    }
}
