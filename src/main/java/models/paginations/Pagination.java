package models.paginations;

public class Pagination {
    private int limit;
    private int next;
    private int prev;
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

    public int getPrev() {
        return prev;
    }

    public void setPrev(int prev) {
        this.prev = prev;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public void setHasNext(int next, int limit, long count) {
        this.hasNext = next >= 0 && (next * limit) < count;
    }

    public boolean hasPrev() {
        return hasPrev;
    }

    public void setHasPrev(int prev) {
        this.hasPrev = prev >= 0;
    }

}
