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
        int limit = handleRequest.getLimit();
        int page  = handleRequest.getPage();
        long count = handleDB.getContributionCounts();

        return new Pagination(limit, page, count);
    }
}
