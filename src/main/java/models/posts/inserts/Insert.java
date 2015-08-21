package models.posts.inserts;

import spark.Request;
import spark.Response;

public class Insert {
    private InsertInterface insertInterface = null;

    public void setInsertInstance(InsertInterface insertInterface) {
        this.insertInterface = insertInterface;
    }

    public String requestInsert(Request request, Response response) {
        return insertInterface.requestInsert(request, response);
    }
}
