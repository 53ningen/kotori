package models.posts;

import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;

public class DeleteContribution extends Status {
    private HandleDB handleDB = new HandleDB();

    public String requestDeleteContribution(Request request, Response response) {

        try {
            DeletePayload payload = new ObjectMapper().readValue(request.body(), DeletePayload.class);
            if (!payload.isValid()) {
                return setBadRequest(response);
            }

            int result = handleDB.deleteContribution(payload.getId());
            if (result < 1) {
                return setBadRequest(response);
            }

            setOK(response);

            return "OK";
        } catch (Exception e) {
            e.printStackTrace();
            return setBadRequest(response);
        }
    }
}
