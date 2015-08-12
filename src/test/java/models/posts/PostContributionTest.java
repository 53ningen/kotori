package models.posts;

import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import databases.DBContributionResource;
import databases.DBNGWordResource;
import helper.RequestHelper;
import helper.ResponseHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.util.stream.Stream;

public class PostContributionTest {
    @Rule
    public final DBContributionResource contributionResource = new DBContributionResource();
    @Rule
    public final DBNGWordResource ngWordResource = new DBNGWordResource();
    private final int LIMIT_NAME_AND_TITLE_LENGTH = 20;
    private PostContribution postContribution;
    private Request request;
    private Response response;

    @Before
    public void setUp() throws Exception {
        postContribution = new PostContribution();
        request = RequestHelper.Requestモックの生成();
        response = ResponseHelper.Responseモックの生成();
    }

    @Test
    public void bodyがnullの場合BadRequestを返す() throws Exception {
        // setup
        when(request.body()).thenReturn(null);

        // exercise
        String errorCode = postContribution.requestPostContribution(request, response);

        // verify
        verify(response).status(400);
        assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID));
    }

    @Test
    public void パラメータが足りない場合BadRequestを返す() throws Exception {
        // setup
        String content = "{\"username\": \"小泉花陽\", \"title\": \"hoge\", \"content\":}";
        when(request.body()).thenReturn(content);

        // exercise
        String errorCode = postContribution.requestPostContribution(request, response);
        
        // verify
        verify(response).status(400);
        assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID));
    }

    @Test
    public void 制限以上の文字数が送られてきた場合BadRequestを返す() throws Exception {
        // setup
        String title = Stream.generate(() -> "a").limit(LIMIT_NAME_AND_TITLE_LENGTH + 1).collect(joining());
        String content = "{\"username\": \"小泉花陽\", \"title\": \""+ title +"\", \"content\": \"hoge\"}";
        when(request.body()).thenReturn(content);

        // exercise
        String errorCode = postContribution.requestPostContribution(request, response);

        // verify
        verify(response).status(400);
        assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID));
    }

    @Test
    public void パラメータが正しければ200OKを返す() throws Exception {
        // setup
        String title = Stream.generate(() -> "a").limit(LIMIT_NAME_AND_TITLE_LENGTH).collect(joining());
        String content = "{\"username\": \"小泉花陽\", \"title\": \""+ title +"\", \"content\": \"hoi\", \"deleteKey\": \"pass\"}}";
        when(request.body()).thenReturn(content);

        // exercise
        postContribution.requestPostContribution(request, response);

        // verify
        verify(response).status(200);
    }

    @Test
    public void パラメータが正しい場合でもNGワードが含まれていればBadRequestを返す() throws Exception {
        // setup
        String title = Stream.generate(() -> "a").limit(LIMIT_NAME_AND_TITLE_LENGTH).collect(joining());
        String content = "{\"username\": \"hoge\", \"title\": \""+ title +"\", \"content\": \"hoi\", \"deleteKey\": \"pass\"}}";
        when(request.body()).thenReturn(content);

        // exercise
        String errorCode = postContribution.requestPostContribution(request, response);

        // verify
        verify(response).status(400);
        assertThat(errorCode, is(ErrorCode.NGWORD_CONTAINS));
    }


}
