package models.paginations;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Pagination {
    private int limit;
    private int next;
    private int current;
    private int prev;
    private List<Page> nextList;
    private List<Page> prevList;
    private boolean hasNext;
    private boolean hasPrev;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getNext() {
        return next;
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public List<Page> getPrevList() {
        return prevList;
    }

    public void setPrevList(int current) {
        // 現在のページから最大3ページ前までのIntStreamを作る
        IntStream s = IntStream.iterate(current - 3, n -> n + 1).limit(3).filter(n -> n > 0);
        this.prevList = s.boxed().map(Page::new).collect(Collectors.toList()); // Pageオブジェクトとしてリストに変換する
    }

    public List<Page> getNextList() {
        return nextList;
    }

    public void setNextList(int current, int limit, long count) {
        // 現在のページから最大3ページ後までのIntStreamを作る
        IntStream s = IntStream.iterate(current + 1, n -> n + 1).limit(3).filter(n -> (n - 1) * limit < count);
        this.nextList = s.boxed().map(Page::new).collect(Collectors.toList()); // Pageオブジェクトとしてリストに変換する
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void setHasNext(int current, int limit, long count) {
        this.hasNext = next > 0 && (current * limit) < count;
    }

    public boolean hasPrev() {
        return hasPrev;
    }

    public void setHasPrev(int prev) {
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
