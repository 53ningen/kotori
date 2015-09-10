package models.paginations;

import models.utils.DBSelectOptions;
import models.requests.HandleRequest;

public class HandlePagination {

    /**
     * ページネーションのインスタンスを作成する
     * @param DBSelectOptions SelectOptionsFactoryインスタンス
     * @param handleRequest HandleRequestインスタンス
     * @return Paginationインスタンス
     */
    public static Pagination createPagination(DBSelectOptions DBSelectOptions, HandleRequest handleRequest) {
        int limit = handleRequest.getLimit();
        int page  = handleRequest.getPage();
        long count = DBSelectOptions.getContributionCounts();

        return new Pagination(limit, page, count);
    }
}
