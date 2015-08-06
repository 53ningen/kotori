package models.paginations;

import models.posts.HandleDB;
import models.requests.HandleRequest;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HandlePaginationTest extends Pagination {
    private final int SHOW_PAGE_NUM = 2;
    private HandlePagination handlePagination;
    private HandleRequest handleRequest;
    private HandleDB handleDB;

    @Before
    public void setUp() throws Exception {
        handlePagination = new HandlePagination();
        handleRequest = mock(HandleRequest.class);
        handleDB = mock(HandleDB.class);
        when(handleRequest.getShowLimit()).thenReturn(10);
        when(handleDB.getContributionCounts()).thenReturn((long) 50);
    }

    @Test
    public void Paginationオブジェクトが正しく生成される() throws Exception {
        // setup
        int page = 3;
        when(handleRequest.getShowPage()).thenReturn(page);

        // exercise
        Pagination pagination = handlePagination.createPagination(handleDB, handleRequest);

        // verify
        assertThat(pagination.getCurrent(), is(page));
        assertThat(pagination.getLimit(), is(10));
        assertThat(pagination.getNext(), is(page + 1));
        assertThat(pagination.hasNext(), is(true));
        assertThat(pagination.getPrev(), is(page - 1));
        assertThat(pagination.hasPrev(), is(true));
        assertThat(pagination.getPrevList(), hasSize(SHOW_PAGE_NUM));
        assertThat(pagination.getPrevList().get(0).getPage(), is(page - 2));
        assertThat(pagination.getPrevList().get(1).getPage(), is(page - 1));
        assertThat(pagination.getNextList(), hasSize(SHOW_PAGE_NUM));
        assertThat(pagination.getNextList().get(0).getPage(), is(page + 1));
        assertThat(pagination.getNextList().get(1).getPage(), is(page + 2));
    }

    @Test
    public void これ以上ページがない場合は空のリストが返される() throws Exception {
        // setup
        int page = 1;
        when(handleRequest.getShowPage()).thenReturn(page);

        // exercise
        Pagination pagination = handlePagination.createPagination(handleDB, handleRequest);

        // verify
        assertThat(pagination.hasPrev(), is(false));
        assertThat(pagination.getPrevList(), is(empty()));
    }

    @Test
    public void 投稿数を超えない分だけのページ番号のリストが返される() throws Exception {
        // setup
        int page = 4;
        when(handleRequest.getShowPage()).thenReturn(page);

        // exercise
        Pagination pagination = handlePagination.createPagination(handleDB, handleRequest);

        // verify
        assertThat(pagination.getNextList(), hasSize(1));
    }
}