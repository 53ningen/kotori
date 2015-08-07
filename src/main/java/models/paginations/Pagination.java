package models.paginations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Pagination {
    private final int SHOW_PAGE_NUM = 2;
    private int limit;
    private int next;
    private int current;
    private int prev;
    private List<Page> nextList;
    private List<Page> prevList;
    private boolean hasNext;
    private boolean hasPrev;

    public Pagination(int limit, int page, long count) {
        this.limit   = limit;
        this.prev    = page - 1;
        this.current = page;
        this.next    = page + 1;
        setPrevList(page);
        setNextList(page, limit, count);
        setHasPrev(page - 1);
        setHasNext(page, limit, count);
    }

    public int getLimit() {
        return limit;
    }

    public int getNext() {
        return next;
    }

    public int getCurrent() {
        return current;
    }

    public int getPrev() {
        return prev;
    }

    public List<Page> getPrevList() {
        return prevList;
    }

    private void setPrevList(int current) {
        // 現在のページから最大SHOW_PAGE_NUMページ前までのIntStreamを作る
        IntStream s = IntStream.iterate(current - SHOW_PAGE_NUM, n -> n + 1).limit(SHOW_PAGE_NUM).filter(n -> n > 0);
        this.prevList = s.boxed().map(Page::new).collect(Collectors.toList()); // Pageオブジェクトとしてリストに変換する
    }

    public List<Page> getNextList() {
        return nextList;
    }

    private void setNextList(int current, int limit, long count) {
        // 現在のページから最大SHOW_PAGE_NUMページ後までのIntStreamを作る
        IntStream s = IntStream.iterate(current + 1, n -> n + 1).limit(SHOW_PAGE_NUM).filter(n -> (n - 1) * limit < count);
        this.nextList = s.boxed().map(Page::new).collect(Collectors.toList()); // Pageオブジェクトとしてリストに変換する
    }

    public boolean hasNext() {
        return hasNext;
    }

    private void setHasNext(int current, int limit, long count) {
        this.hasNext = next > 0 && (current * limit) < count;
    }

    public boolean hasPrev() {
        return hasPrev;
    }

    private void setHasPrev(int prev) {
        this.hasPrev = prev > 0;
    }

    protected class Page {
        private int page;

        public Page(int page) {
            this.page = page;
        }

        public int getPage() {
            return page;
        }
    }

}
