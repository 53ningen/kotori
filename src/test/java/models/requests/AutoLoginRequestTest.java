package models.requests;

import databases.entities.User;
import databases.resources.DBAutoLoginResource;
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
public class AutoLoginRequestTest {

    public static class 挿入テスト {
        private AutoLoginRequest autoLoginRequest = new AutoLoginRequest();
        private Response response;

        @Before
        public void setUp() throws Exception {
            response = ResponseHelper.Responseモックの生成();
        }

        @Test
        public void Userインスタンスのフィールドがnullの場合InternalServerErrorを返す() throws Exception {
            // setup
            User user = new User();

            // exercise
            autoLoginRequest.insert(user, response);

            // verify
            verify(response).status(500);
        }

        @Test
        public void パラメータが正しければ200OKを返す() throws Exception {
            // setup
            User user = new User("id", "name");

            // exercise
            autoLoginRequest.insert(user, response);

            // verify
            verify(response).status(200);
        }
    }

    public static class 削除テスト {
        @Rule
        public final DBAutoLoginResource autoLoginResource = new DBAutoLoginResource();
        private final String AUTH_TOKEN = "auth_token";
        private AutoLoginRequest autoLoginRequest = new AutoLoginRequest();
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
            when(request.cookie(AUTH_TOKEN)).thenReturn(null);

            // exercise
            String errorCode = autoLoginRequest.delete(request, response);

            // verify
            verify(response).status(400);
            assertThat(errorCode, CoreMatchers.is(ErrorCode.PARAMETER_INVALID.getErrorMsg()));
        }

        @Test
        public void キーがDBに存在しない場合InternalServerErrorを返す() throws Exception {
            // setup
            when(request.cookie(AUTH_TOKEN)).thenReturn("");

            // exercise
            autoLoginRequest.delete(request, response);

            // verify
            verify(response).status(500);
        }

        @Test
        public void パラメータが正しければ200OKを返す() throws Exception {
            // setup
            when(request.cookie(AUTH_TOKEN)).thenReturn("testuser_testtoken");

            // exercise
            autoLoginRequest.delete(request, response);

            // verify
            verify(response).status(200);
        }
    }
}
