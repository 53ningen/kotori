package kotori;

import routes.ApplicationRoute;

import java.util.stream.Stream;

public class Global {

    public static void main(String args[]) {

        Redis.getRedis();
        MySQL.setupDatabase();
        ApplicationRoute application = ApplicationRoute.getApplicationRoute();

        try {
            Stream.of(args).findFirst().ifPresent(arg -> application.initServerPort(Integer.valueOf(arg)));
        } catch (IllegalArgumentException e) {
            application.initServerPort(9000);
        } finally {
            application.initServerRoutes();
        }

    }

}
