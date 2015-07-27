package helper;

import static org.mockito.Mockito.*;
import spark.Request;

public class RequestHelper {
    public static Request Requestモックの生成() {
        Request request = mock(Request.class);
        return request;
    }
}


