package models.posts.updates;

import spark.Request;
import spark.Response;

public class Update {
    private UpdateInterface updateInterface = null;

    public void setUpdateInstance(UpdateInterface updateInterface) {
        this.updateInterface = updateInterface;
    }

    public String requestUpdate(Request request, Response response) {
        return updateInterface.requestUpdate(request, response);
    }}
