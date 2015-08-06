package models.paginations;

import models.posts.HandleDB;
import models.requests.HandleRequest;

public class HandlePagination {

    public Pagination createPagination(HandleDB handleDB, HandleRequest handleRequest) {
        Pagination pagination = new Pagination();
        int limit = handleRequest.getShowLimit();
        int page  = handleRequest.getShowPage();
        long count = handleDB.getContributionCounts();

        System.out.print("limit:"+limit+" page:"+page+" count:"+count);

        pagination.setLimit(limit);
        pagination.setPrev(page - 1);
        pagination.setNext(page + 1);
        pagination.setHasPrev(page - 1);
        pagination.setHasNext(page + 1, limit, count);

        return pagination;
    }
}
