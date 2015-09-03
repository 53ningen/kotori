package models.posts.utils;

import models.requests.HandleRequest;
import org.seasar.doma.jdbc.SelectOptions;

public class DBSelectOptions {
    private static DBSelectOptions dbSelectOptions = new DBSelectOptions();
    private SelectOptions options;

    private DBSelectOptions() {
        options = SelectOptions.get();
    }

    public static DBSelectOptions getDBSelectOptions() {
        return dbSelectOptions;
    }

    public SelectOptions setOptions(HandleRequest req) {
        int page = req.getPage() - 1;
        int limit = req.getLimit();
        return options.offset(page * limit).limit(limit).count();
    }

    public SelectOptions setOptions(int limit) {
        return options.limit(limit);
    }

    /**
     * DBに格納されている投稿件数を返す
     * @return 投稿件数
     */
    public long getContributionCounts() {
        try {
            return options.getCount();
        } catch (NullPointerException e) {
            return 0;
        }
    }
}
