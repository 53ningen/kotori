package models.requests;

import databases.resources.*;
import helper.RequestHelper;
import helper.ResponseHelper;
import models.posts.utils.ErrorCode;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import spark.Request;
import spark.Response;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class ContributionRequestTest {

    public static class 挿入テスト {
        @Rule
        public final DBContributionResource contributionResource = new DBContributionResource();
        @Rule
        public final DBNGWordResource ngWordResource = new DBNGWordResource();
        @Rule
        public final DBNGUserResource ngUserResource = new DBNGUserResource();
        @Rule
        public final DBUserResource userResource = new DBUserResource();
        @Rule
        public final DBAutoLoginResource autoLoginResource = new DBAutoLoginResource();

        private ContributionRequest contributionRequest = new ContributionRequest();
        private Request request;
        private Response response;
        private final int LIMIT_NAME_AND_TITLE_LENGTH = 20;

        @Before
        public void setUp() throws Exception {
            request = RequestHelper.Requestモックの生成();
            response = ResponseHelper.Responseモックの生成();
        }

        @Test
        public void bodyがnullの場合BadRequestを返す() throws Exception {
            // setup
            when(request.body()).thenReturn(null);

            // exercise
            String errorCode = contributionRequest.insert(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, CoreMatchers.is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが足りない場合BadRequestを返す() throws Exception {
            // setup
            String content = "{\"title\": \"hoge\", \"content\":}";
            when(request.body()).thenReturn(content);

            // exercise
            String errorCode = contributionRequest.insert(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void 制限以上の文字数が送られてきた場合BadRequestを返す() throws Exception {
            // setup
            String title = Stream.generate(() -> "a").limit(LIMIT_NAME_AND_TITLE_LENGTH + 1).collect(joining());
            String content = "{\"title\": \"" + title + "\", \"content\": \"hoge\"}";
            when(request.body()).thenReturn(content);

            // exercise
            String errorCode = contributionRequest.insert(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが正しければ200OKを返す() throws Exception {
            // setup
            String title = Stream.generate(() -> "a").limit(LIMIT_NAME_AND_TITLE_LENGTH).collect(joining());
            String content = "{\"title\": \"" + title + "\", \"content\": \"hoi\"}";
            when(request.body()).thenReturn(content);
            when(request.cookie("auth_token")).thenReturn("testuser_testtoken");

            // exercise
            contributionRequest.insert(request, response);

            // verify
            verify(response).status(200);
        }

        @Test
        public void パラメータが正しい場合でもNGワードが含まれていればBadRequestを返す() throws Exception {
            // setup
            String title = Stream.generate(() -> "a").limit(LIMIT_NAME_AND_TITLE_LENGTH).collect(joining());
            String content = "{\"title\": \"" + title + "\", \"content\": \"hoge\"}";
            when(request.body()).thenReturn(content);

            // exercise
            String errorCode = contributionRequest.insert(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, is(ErrorCode.NGWORD_CONTAINS.getErrorMsg()));
        }
    }

    public static class 削除テスト {
        @Rule
        public final DBContributionResource contributionResource = new DBContributionResource();
        @Rule
        public final DBAutoLoginResource autoLoginResource = new DBAutoLoginResource();
        private ContributionRequest contributionRequest = new ContributionRequest();
        private Request request;
        private Response response;

        @Before
        public void setUp() throws Exception {
            request = RequestHelper.Requestモックの生成();
            response = ResponseHelper.Responseモックの生成();
        }

        @Test
        public void bodyがnullの場合BadRequestを返す() throws Exception {
            // setup
            when(request.body()).thenReturn(null);

            // exercise
            String errorCode = contributionRequest.delete(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, CoreMatchers.is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが足りない場合BadRequestを返す() throws Exception {
            // setup
            String content = "{\"id\": \"1\"}";
            when(request.body()).thenReturn(content);

            // exercise
            String errorCode = contributionRequest.delete(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが正しければ200OKを返す() throws Exception {
            // setup
            String content = "{\"id\": \"1\"}";
            when(request.body()).thenReturn(content);
            when(request.cookie("auth_token")).thenReturn("hanayo_token");

            // exercise
            contributionRequest.delete(request, response);

            // verify
            verify(response).status(200);
        }

        @Test
        public void Adminからのパラメータが正しければ200OKを返す() throws Exception {
            // setup
            String content = "{\"id\": \"1\"}";
            when(request.body()).thenReturn(content);

            // exercise
            contributionRequest.deleteWithoutKey(request, response);

            // verify
            verify(response).status(200);
        }
    }

    public static class 更新テスト {
        @Rule
        public final DBContributionResource contributionResource = new DBContributionResource();
        private ContributionRequest contributionRequest = new ContributionRequest();
        private Request request;
        private Response response;

        @Before
        public void setUp() throws Exception {
            request = RequestHelper.Requestモックの生成();
            response = ResponseHelper.Responseモックの生成();
        }

        @Test
        public void bodyがnullの場合BadRequestを返す() throws Exception {
            // setup
            when(request.body()).thenReturn(null);

            // exercise
            String errorCode = contributionRequest.update(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, CoreMatchers.is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが足りない場合BadRequestを返す() throws Exception {
            // setup
            String content = "{\"id\": \"1\", \"content\":}";
            when(request.body()).thenReturn(content);

            // exercise
            String errorCode = contributionRequest.update(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが正しければ200OKを返す() throws Exception {
            // setup
            String content = "{\"id\": \"1\", \"content\": \"小泉花陽\"}";
            when(request.body()).thenReturn(content);

            // exercise
            contributionRequest.update(request, response);

            // verify
            verify(response).status(200);
        }
    }
}
