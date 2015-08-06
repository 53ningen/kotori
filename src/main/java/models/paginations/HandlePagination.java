package models.paginations;

import models.posts.HandleDB;
import models.requests.HandleRequest;

public class HandlePagination {

    /**
     * ページネーションのインスタンスを作成する
     * @param handleDB HandleDBインスタンス
     * @param handleRequest HandleRequestインスタンス
     * @return Paginationインスタンス
     */
    public Pagination createPagination(HandleDB handleDB, HandleRequest handleRequest) {
        Pagination pagination = new Pagination();
        int limit = handleRequest.getShowLimit();
        int page  = handleRequest.getShowPage();
        long count = handleDB.getContributionCounts();

        pagination.setLimit(limit);
        pagination.setPrev(page - 1);
        pagination.setCurrent(page);
        pagination.setNext(page + 1);
        pagination.setPrevList(page);
        pagination.setNextList(page, limit, count);
        pagination.setHasPrev(page - 1);
        pagination.setHasNext(page, limit, count);

        return pagination;
    }
}
