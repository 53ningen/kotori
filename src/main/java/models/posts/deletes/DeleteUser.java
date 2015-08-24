package models.posts.deletes;

import com.fasterxml.jackson.databind.ObjectMapper;
import databases.entities.User;
import models.payloads.DeletePayload;
import models.posts.handles.HandleDB;
import models.posts.utils.ErrorCode;
import spark.Request;
import spark.Response;

public class DeleteUser implements DeleteInterface {
    private static final DeleteUser deleteUser = new DeleteUser();

    public static DeleteUser getDeleteUser() {
        return deleteUser;
    }

    /**
     * ユーザの削除を受け付ける
     * @param request リクエスト
     * @param response レスポンス
     * @return ok
     */
    @Override
    public String requestDelete(Request request, Response response) {

        try {
            User user = new ObjectMapper().readValue(request.body(), User.class);
            if (!user.isValid()) {
                return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
            }

            int result = HandleDB.user().delete(user);
            if (result < 1) {
                return setInternalServerError(response);
            }

            setOK(response);

            return "ok";
        } catch (Exception e) {
            return setBadRequest(response, ErrorCode.PARAMETER_INVALID);
        }
    }
}
