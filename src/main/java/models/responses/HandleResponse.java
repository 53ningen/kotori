package models.responses;

import databases.entities.Contribution;
import databases.entities.NGInterface;
import databases.entities.User;
import models.paginations.Pagination;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HandleResponse {
    private HashMap<String, Object> responseMap = new HashMap<>();

    public HashMap<String, Object> getResponseMap() {
        return responseMap;
    }

    public HandleResponse(Request request,
                          List<Contribution> contributions,
                          Optional<User> userOpt,
                          Pagination pagination,
                          String query) {
        setContributions(contributions);
        setUser(userOpt);
        setPagination(pagination);
        setPathInfo(request);
        setQueryMap(request, query);
    }

    public <T> HandleResponse(Request request, List<T> list) {
        setList(list);
        setPathInfo(request);
    }

    public <T> HandleResponse(Request request, Map<String, T> map) {
        setMap(map);
        setPathInfo(request);
    }

    public HandleResponse(Request request) {
        setPathInfo(request);
    }

    private <T> void setList(List<T> list) {
        responseMap.put("list", list);
    }

    private <T> void setMap(Map<String, T> map) {
        map.forEach(responseMap::put);
    }

    private void setContributions(List<Contribution> contributions) {
        responseMap.put("contributions", contributions);
    }

    private void setUser(Optional<User> userOpt) {
        userOpt.ifPresent(user -> responseMap.put("user", user));
    }

    private void setPagination(Pagination pagination) {
        responseMap.put("pagination", pagination);
    }

    private void setPathInfo(Request request) {
        responseMap.put("path", request.pathInfo());
    }

    /**
     * URLの該当するクエリマップをHashMapとして返す
     * @param request リクエスト
     * @param query 絞り込むクエリ文字列
     */
    private void setQueryMap(Request request, String query) {
        if (query.isEmpty()) return;
        Map<String, String> queryMap = new HashMap<>();
        request.queryMap(query).toMap().keySet().forEach(param -> {
            param = query + "[" + param + "]";
            queryMap.put(param, request.queryParams(param));
        });
        responseMap.put("query", queryMap.entrySet());
    }


}
