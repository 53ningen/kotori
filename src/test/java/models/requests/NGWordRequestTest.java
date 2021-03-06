package models.requests;

import databases.resources.DBNGWordResource;
import helper.RequestHelper;
import helper.ResponseHelper;
import models.utils.ErrorCode;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import spark.Request;
import spark.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class NGWordRequestTest {

    public static class 挿入テスト {
        @Rule
        public final DBNGWordResource ngWordResource = new DBNGWordResource();
        private NGWordRequest ngWordRequest = new NGWordRequest();
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
            String errorCode = ngWordRequest.insert(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, CoreMatchers.is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが足りない場合BadRequestを返す() throws Exception {
            // setup
            String content = "{\"word\":}";
            when(request.body()).thenReturn(content);

            // exercise
            String errorCode = ngWordRequest.insert(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが正しければ200OKを返す() throws Exception {
            // setup
            String content = "{\"word\": \"hoge\"}";
            when(request.body()).thenReturn(content);

            // exercise
            ngWordRequest.insert(request, response);

            // verify
            verify(response).status(200);
        }
    }

    public static class 削除テスト {
        @Rule
        public final DBNGWordResource ngWordResource = new DBNGWordResource();
        private NGWordRequest ngWordRequest = new NGWordRequest();
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
            String errorCode = ngWordRequest.delete(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, CoreMatchers.is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが足りない場合BadRequestを返す() throws Exception {
            // setup
            String content = "{\"id\":}";
            when(request.body()).thenReturn(content);

            // exercise
            String errorCode = ngWordRequest.delete(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void パラメータが正しければ200OKを返す() throws Exception {
            // setup
            String content = "{\"id\": \"1\"}";
            when(request.body()).thenReturn(content);

            // exercise
            ngWordRequest.delete(request, response);

            // verify
            verify(response).status(200);
        }
    }
}
