package models.responses;

import databases.entities.Contribution;
import models.paginations.Pagination;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleResponse {
    HashMap<String, Object> responseMap = new HashMap<>();

    public HashMap<String, Object> getResponseMap() {
        return responseMap;
    }

    public void setResponseMap(Request request, List<Contribution> contributions, Pagination pagination, String query) {
        setContributions(contributions);
        setPagination(pagination);
        setPathInfo(request);
        setQueryMap(request, query);
    }

    private void setContributions(List<Contribution> contributions) {
        responseMap.put("contributions", contributions);
    }

    private void setPagination(Pagination pagination) {
        responseMap.put("pagination", pagination);
    }

    private void setPathInfo(Request request) {
        responseMap.put("path", request.pathInfo());
    }

    private void setQueryMap(Request request, String query) {
        Map<String, String> queryMap = new HashMap<>();
        request.queryMap(query).toMap().keySet().forEach(param -> {
            param = query + "[" + param + "]";
            queryMap.put(param, request.queryParams(param));
        });
        responseMap.put("query", queryMap.entrySet());
    }


}
