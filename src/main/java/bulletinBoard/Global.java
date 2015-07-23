package bulletinBoard;

import routes.ApplicationRoute;

public class Global {

    public static void main(String args[]) {

        ApplicationRoute applicationRoute = ApplicationRoute.getApplicationRoute();
        applicationRoute.initServerConf();

    }
}
