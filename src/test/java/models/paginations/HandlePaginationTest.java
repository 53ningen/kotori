package models.paginations;

import models.posts.utils.DBSelectOptions;
import models.requests.HandleRequest;
import org.junit.Before;
import org.junit.Test;

import static models.paginations.HandlePagination.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HandlePaginationTest {
    private final int SHOW_PAGE_NUM = 2;
    private HandleRequest handleRequest;
    private DBSelectOptions dbSelectOptions;

    @Before
    public void setUp() throws Exception {
        handleRequest = mock(HandleRequest.class);
        dbSelectOptions = mock(DBSelectOptions.class);
        when(handleRequest.getLimit()).thenReturn(10);
        when(dbSelectOptions.getContributionCounts()).thenReturn((long) 50);
    }

    @Test
    public void Paginationオブジェクトが正しく生成される() throws Exception {
        // setup
        int page = 3;
        when(handleRequest.getPage()).thenReturn(page);

        // exercise
        Pagination pagination = createPagination(dbSelectOptions, handleRequest);

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
        when(handleRequest.getPage()).thenReturn(page);

        // exercise
        Pagination pagination = createPagination(dbSelectOptions, handleRequest);

        // verify
        assertThat(pagination.hasPrev(), is(false));
        assertThat(pagination.getPrevList(), is(empty()));
    }

    @Test
    public void 投稿数を超えない分だけのページ番号のリストが返される() throws Exception {
        // setup
        int page = 4;
        when(handleRequest.getPage()).thenReturn(page);

        // exercise
        Pagination pagination = createPagination(dbSelectOptions, handleRequest);

        // verify
        assertThat(pagination.getNextList(), hasSize(1));
    }

    @Test
    public void 投稿数を超えたページ番号を参照した場合ページネーションを行わない() throws Exception {
        // setup
        int page = 6;
        when(handleRequest.getPage()).thenReturn(page);

        // exercise
        Pagination pagination = createPagination(dbSelectOptions, handleRequest);

        // verify
        assertThat(pagination.getCurrent(), is(1));
        assertThat(pagination.hasPrev(), is(false));
        assertThat(pagination.hasNext(), is(false));
    }

    @Test
    public void 検索結果が0件の場合ページネーションを行わない() throws Exception {
        // setup
        int page = 2;
        when(handleRequest.getPage()).thenReturn(page);
        when(dbSelectOptions.getContributionCounts()).thenReturn((long) 0);

        // exercise
        Pagination pagination = createPagination(DBSelectOptions.getDBSelectOptions(), handleRequest);

        // verify
        assertThat(pagination.getCurrent(), is(1));
        assertThat(pagination.hasPrev(), is(false));
        assertThat(pagination.hasNext(), is(false));
    }

}