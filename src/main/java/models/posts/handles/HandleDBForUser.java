package models.posts.handles;

import bulletinBoard.DBConfig;
import databases.daos.UserDao;
import databases.entities.User;
import helper.DaoImplHelper;
import org.seasar.doma.jdbc.tx.TransactionManager;

public class HandleDBForUser extends HandleDB {
    private final UserDao userDao = DaoImplHelper.get(UserDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    /**
     * UserをDBに格納する
     * @param user Userインスタンス
     * @return 処理した投稿数
     */
    public int insert(User user) {
        return tm.required(() -> userDao.insert(user));
    }

    /**
     * UserをDBから削除する
     * @param user Userインスタンス
     * @return 処理した投稿数
     */
    public int delete(User user) {
        return tm.required(() -> userDao.delete(user));
    }
}
