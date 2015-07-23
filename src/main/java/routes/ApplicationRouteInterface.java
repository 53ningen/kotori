package routes;

import spark.ModelAndView;
import spark.Request;
import spark.Response;

/**
 * ルーティングインタフェース
 */
public interface ApplicationRouteInterface {
    ModelAndView getRoot(Request req, Response res);
    ModelAndView getStop(Request req, Response res);
}
