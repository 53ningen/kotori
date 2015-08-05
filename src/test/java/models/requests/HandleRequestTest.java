package models.requests;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import helper.RequestHelper;
import org.junit.Before;
import org.junit.Test;
import spark.QueryParamsMap;
import spark.Request;


public class HandleRequestTest {
    private final int DEFAULT_LIMIT = 10;
    private Request request;

    @Before
    public void setUp() throws Exception {
        request = RequestHelper.Requestモックの生成();
    }

    @Test
    public void リクエストのクエリから表示件数を返す() throws Exception {
        // setup
        QueryParamsMap map = mock(QueryParamsMap.class);
        when(request.queryMap()).thenReturn(map);
        when(map.get("show", "limit")).thenReturn(map);
        when(map.integerValue()).thenReturn(30);

        // exercise
        int limit = HandleRequest.setShowLimit(request);

        // verify
        assertThat(limit, is(30));
    }

    @Test
    public void リクエストのクエリが0以下ならばデフォルトの表示件数を返す() throws Exception {
        // setup
        QueryParamsMap map = mock(QueryParamsMap.class);
        when(request.queryMap()).thenReturn(map);
        when(map.get("show", "limit")).thenReturn(map);
        when(map.integerValue()).thenReturn(-1);

        // exercise
        int limit = HandleRequest.setShowLimit(request);

        // verify
        assertThat(limit, is(DEFAULT_LIMIT));
    }

    @Test
    public void リクエストのクエリがnullならばデフォルトの表示件数を返す() throws Exception {
        // setup
        when(request.queryMap()).thenReturn(null);

        // exercise
        int limit = HandleRequest.setShowLimit(request);

        // verify
        assertThat(limit, is(DEFAULT_LIMIT));
    }
}
