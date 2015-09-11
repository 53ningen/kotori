package bulletinBoard;

import routes.ApplicationRoute;

import java.util.stream.Stream;

public class Global {

    public static void main(String args[]) {

        RedisServer.getRedisServer();
        ApplicationRoute application = ApplicationRoute.getApplicationRoute();

        Stream.of(args).findFirst().ifPresent(arg -> {
            try {
                application.initServerPort(Integer.valueOf(arg));
            } catch (IllegalArgumentException e) {
                application.initServerPort(9000);
            } finally {
                application.initServerRoutes();
            }
        });

    }

}
