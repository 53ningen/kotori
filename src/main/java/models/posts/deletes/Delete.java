package models.posts.deletes;

import spark.Request;
import spark.Response;

public class Delete {
    private DeleteInterface deleteInterface = null;

    public void setDeleteInstance(DeleteInterface deleteInterface) {
        this.deleteInterface = deleteInterface;
    }

    public String requestDelete(Request request, Response response) {
        return deleteInterface.requestDelete(request, response);
    }

    public String requestDeleteWithoutKey(Request request, Response response) {
        return  deleteInterface.requestDeleteWithoutKey(request, response);
    }
}
