package models.posts.handles;

import bulletinBoard.DBConfig;
import databases.daos.AutoLoginDao;
import databases.entities.AutoLogin;
import helper.DaoImplHelper;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.Optional;

public class HandleDBForAutoLogin extends HandleDB {
    private final AutoLoginDao autoLoginDao = DaoImplHelper.get(AutoLoginDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    /**
     * AutoLogin情報をDBから取得する
     * @param token トークン
     * @return Optional型のAutoLoginインスタンス
     */
    public Optional<AutoLogin> selectByToken(String token) {
        return tm.required(() -> autoLoginDao.selectByToken(token));
    }

    /**
     * AutoLogin情報をDBに格納する
     * @param al AutoLoginインスタンス
     * @return 処理した件数
     */
    public int insert(AutoLogin al) {
        return tm.required(() -> autoLoginDao.insert(al));
    }

    /**
     * AutoLogin情報をDBから削除する
     * @param al AutoLoginインスタンス
     * @return 処理した件数
     */
    public int delete(AutoLogin al) {
        return tm.required(() -> autoLoginDao.delete(al));
    }

    /**
     * AutoLogin情報をDBからID指定で一括削除する
     * @param userid ユーザid
     * @return 処理した件数
     */
    public int deleteByUserId(String userid) {
        return tm.required(() -> autoLoginDao.deleteByUserId(userid));
    }
}
