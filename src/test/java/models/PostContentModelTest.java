package models;

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

    @Test
    public void bodyがnullの場合BadRequestを返す() throws Exception {
        // setup
        when(request.body()).thenReturn(null);

        // exercise
        postContentModel.requestPostContent(request, response);

        // verify
        verify(response).status(400);
    }

    @Test
    public void contentのパラメータが空の場合BadRequestを返す() throws Exception {
        // setup
        String content = "{\"content\":}";
        when(request.body()).thenReturn(content);

        // exercise
        postContentModel.requestPostContent(request, response);
        
        // verify
        verify(response).status(400);
    }

    @Test
    public void contentのパラメータが正しければ200OKを返す() throws Exception {
        // setup
        String content = "{\"content\": \"hoge\"}";
        when(request.body()).thenReturn(content);

        // exercise
        String id = postContentModel.requestPostContent(request, response);

        // verify
        verify(response).status(200);
    }

}
