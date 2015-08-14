package models.posts.deletes;

import databases.DBNGUserResource;
import helper.RequestHelper;
import helper.ResponseHelper;
import models.posts.utils.ErrorCode;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import spark.Request;
import spark.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeleteNGUserTest {
    @Rule
    public final DBNGUserResource resource = new DBNGUserResource();
    private DeleteNGUser deleteNGUser;
    private Request request;
    private Response response;

    @Before
    public void setUp() throws Exception {
        deleteNGUser = new DeleteNGUser();
        request = RequestHelper.Requestモックの生成();
        response = ResponseHelper.Responseモックの生成();
    }

    @Test
    public void bodyがnullの場合BadRequestを返す() throws Exception {
        // setup
        when(request.body()).thenReturn(null);

        // exercise
        String errorCode = deleteNGUser.requestDelete(request, response);

        // verify
        verify(response).status(400);
        assertThat(errorCode, CoreMatchers.is(ErrorCode.PARAMETER_INVALID));
    }

    @Test
    public void パラメータが足りない場合BadRequestを返す() throws Exception {
        // setup
        String content = "{\"id\":}";
        when(request.body()).thenReturn(content);

        // exercise
        String errorCode = deleteNGUser.requestDelete(request, response);

        // verify
        verify(response).status(400);
        assertThat(errorCode, is(ErrorCode.PARAMETER_INVALID));
    }

    @Test
    public void パラメータが正しければ200OKを返す() throws Exception {
        // setup
        String content = "{\"id\": \"1\"}";
        when(request.body()).thenReturn(content);

        // exercise
        deleteNGUser.requestDelete(request, response);

        // verify
        verify(response).status(200);
    }
}
