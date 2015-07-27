package models;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import helper.RequestHelper;
import helper.ResponseHelper;
import models.posts.PostContentModel;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

public class PostContentModelTest {
    private PostContentModel postContentModel;
    private Request request;
    private Response response;

    @Before
    public void setUp() throws Exception {
        postContentModel = new PostContentModel();
        request = RequestHelper.Requestモックの生成();
        response = ResponseHelper.Responseモックの生成();
    }

    @Test (expected = RuntimeException.class)
    public void bodyが空の場合RuntimeExceptionを返す() throws Exception {
        // setup
        when(request.body()).thenReturn("");

        // verify
        postContentModel.requestPostContent(request, response);
    }

    @Test
    public void contentのパラメータが空の場合BadRequestを返す() throws Exception {
        // setup
        String content = "{\"content\":}";
        when(request.body()).thenReturn(content);

        // verify
        postContentModel.requestPostContent(request, response);
        verify(response).status(400);
    }

    @Test
    public void contentのパラメータが正しければ200OKを返しid1を割り当てる() throws Exception {
        // setup
        String content = "{\"content\": \"hoge\"}";
        when(request.body()).thenReturn(content);

        // verify
        String id = postContentModel.requestPostContent(request, response);
        verify(response).status(200);
        assertThat(id, is("1"));
    }

    @Test
    public void 投稿idがインクリメントされる() throws Exception {
        // setup
        String content = "{\"content\": \"hoge\"}";
        when(request.body()).thenReturn(content);

        // verify
        String id = postContentModel.requestPostContent(request, response);
        String id2 = postContentModel.requestPostContent(request, response);

        assertThat(id, is("1"));
        assertThat(id2, is("2"));
    }

}
