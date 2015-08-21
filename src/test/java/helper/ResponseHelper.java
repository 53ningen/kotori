package helper;

import static org.mockito.Mockito.*;
import spark.Response;

public class ResponseHelper {
    public static Response Responseモックの生成() {
        Response response = mock(Response.class);
        return response;
    }
}
