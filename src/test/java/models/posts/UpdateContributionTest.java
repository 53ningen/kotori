package models.posts;

import databases.DBResource;
import helper.RequestHelper;
import helper.ResponseHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import spark.Request;
import spark.Response;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UpdateContributionTest {
    @Rule
    public final DBResource resource = new DBResource();
    private UpdateContribution updateContribution;
    private Request request;
    private Response response;

    @Before
    public void setUp() throws Exception {
        updateContribution = new UpdateContribution();
        request = RequestHelper.Requestモックの生成();
        response = ResponseHelper.Responseモックの生成();
    }

    @Test
    public void bodyがnullの場合BadRequestを返す() throws Exception {
        // setup
        when(request.body()).thenReturn(null);

        // exercise
        updateContribution.requestUpdateContribution(request, response);

        // verify
        verify(response).status(400);
    }

    @Test
    public void パラメータが足りない場合BadRequestを返す() throws Exception {
        // setup
        String content = "{\"id\": \"1\", \"content\":}";
        when(request.body()).thenReturn(content);

        // exercise
        updateContribution.requestUpdateContribution(request, response);

        // verify
        verify(response).status(400);
    }

    @Test
    public void パラメータが正しければ200OKを返す() throws Exception {
        // setup
        String content = "{\"id\": \"1\", \"content\": \"小泉花陽\"}";
        when(request.body()).thenReturn(content);

        // exercise
        updateContribution.requestUpdateContribution(request, response);

        // verify
        verify(response).status(200);
    }
}
