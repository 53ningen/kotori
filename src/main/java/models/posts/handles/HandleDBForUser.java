package models.posts.handles;

import bulletinBoard.DBConfig;
import databases.daos.UserDao;
import databases.entities.User;
import helper.DaoImplHelper;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.Optional;

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
     * UserをDBから取り出す
     * @param user Userインスタンス
     * @return Optional型のUserインスタンス
     */
    public Optional<User> select(User user) {
        return tm.required(() -> userDao.select(user.getUserid(), user.getPassword()));
    }

    /**
     * AdminUserをDBから取得する
     * @param userid ユーザID
     * @return 該当ユーザがAdminであればOptionalのUserインスタンスを返す
     */
    public Optional<User> selectAdminUser(String userid) {
        return tm.required(() -> userDao.selectAdminUser(userid));
    }

    /**
     * UserをDBから削除する
     * @param user Userインスタンス
     * @return 処理した投稿数
     */
    public int delete(User user) {
        return tm.required(() -> userDao.delete(user));
    }

    /**
     * 指定したuseridのusernameを取得する
     * @param userid ユーザID
     * @return ユーザ名
     */
    public String selectUsername(String userid) {
        return tm.required(() -> userDao.selectUsername(userid));
    }
}
