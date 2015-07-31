package models.posts;

import static java.util.stream.Collectors.*;
import static org.mockito.Mockito.*;

import helper.RequestHelper;
import helper.ResponseHelper;
import org.junit.Before;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.util.stream.Stream;

public class PostContentModelTest {
    private final int LIMIT_TITLE_LENGTH = 20;
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
    public void パラメータが足りない場合BadRequestを返す() throws Exception {
        // setup
        String content = "{\"title\": \"hoge\", \"content\":}";
        when(request.body()).thenReturn(content);

        // exercise
        postContentModel.requestPostContent(request, response);
        
        // verify
        verify(response).status(400);
    }

    @Test
    public void 制限以上の文字数が送られてきた場合BadRequestを返す() throws Exception {
        // setup
        String title = Stream.generate(() -> "a").limit(LIMIT_TITLE_LENGTH + 1).collect(joining());
        String content = "{\"title\": \""+ title +"\", \"content\": \"hoge\"}";
        when(request.body()).thenReturn(content);

        // exercise
        postContentModel.requestPostContent(request, response);

        // verify
        verify(response).status(400);
    }

    @Test
    public void パラメータが正しければ200OKを返す() throws Exception {
        // setup
        String title = Stream.generate(() -> "a").limit(LIMIT_TITLE_LENGTH).collect(joining());
        String content = "{\"title\": \""+ title +"\", \"content\": \"hoge\"}";
        when(request.body()).thenReturn(content);

        // exercise
        postContentModel.requestPostContent(request, response);

        // verify
        verify(response).status(200);
    }

}
