package models.posts;

import static java.util.stream.Collectors.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.databind.ObjectMapper;
import databases.DBResource;
import helper.RequestHelper;
import helper.ResponseHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import spark.Request;
import spark.Response;

import java.lang.reflect.Method;
import java.util.stream.Stream;

public class PostContributionTest {
    @Rule
    public final DBResource resource = new DBResource();
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
        postContribution.requestPostContribution(request, response);

        // verify
        verify(response).status(400);
    }

    @Test
    public void パラメータが足りない場合BadRequestを返す() throws Exception {
        // setup
        String content = "{\"username\": \"小泉花陽\", \"title\": \"hoge\", \"content\":}";
        when(request.body()).thenReturn(content);

        // exercise
        postContribution.requestPostContribution(request, response);
        
        // verify
        verify(response).status(400);
    }

    @Test
    public void 制限以上の文字数が送られてきた場合BadRequestを返す() throws Exception {
        // setup
        String title = Stream.generate(() -> "a").limit(LIMIT_NAME_AND_TITLE_LENGTH + 1).collect(joining());
        String content = "{\"username\": \"小泉花陽\", \"title\": \""+ title +"\", \"content\": \"hoge\"}";
        when(request.body()).thenReturn(content);

        // exercise
        postContribution.requestPostContribution(request, response);

        // verify
        verify(response).status(400);
    }

    @Test
    public void パラメータが正しければ200OKを返す() throws Exception {
        // setup
        String title = Stream.generate(() -> "a").limit(LIMIT_NAME_AND_TITLE_LENGTH).collect(joining());
        String content = "{\"username\": \"小泉花陽\", \"title\": \""+ title +"\", \"content\": \"hoge\"}";
        when(request.body()).thenReturn(content);

        // exercise
        postContribution.requestPostContribution(request, response);

        // verify
        verify(response).status(200);
    }

    @Test
    public void unicodeエスケープされた文字列を元に戻す() throws Exception {
        // setup
        String escapeStr = "{\"username\":\"\\\\u897f\\\\u6728\\\\u91ce\\\\u771f\\\\u59eb\", \"content\":\"\\\\u000a\"}";
        Method method = postContribution.getClass().getDeclaredMethod("unescapeUnicode", String.class);
        method.setAccessible(true);

        // exercise
        String unescapeStr = (String) method.invoke(postContribution, escapeStr);

        // verify
        assertThat(unescapeStr, is("{\"username\":\"西木野真姫\", \"content\":\"\\\\n\"}"));
    }

    @Test
    public void HTMLタグを除いた文字列を返す() throws Exception {
        // setup
        String str = "{\"content\":\"hoge<b>foo</b>bar\"}";
        Method method = postContribution.getClass().getDeclaredMethod("validateBody", String.class);
        method.setAccessible(true);

        // exercise
        String excludedStr = (String) method.invoke(postContribution, str);

        // verify
        assertThat(excludedStr, is("{\"content\":\"hogefoobar\"}"));
    }
}
