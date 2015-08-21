package models.posts.handles;

import bulletinBoard.DBConfig;
import databases.daos.NGUserDao;
import databases.entities.NGUser;
import helper.DaoImplHelper;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.List;

public class HandleDBForNGUser {
    private final NGUserDao ngUserDao = DaoImplHelper.get(NGUserDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    /**
     * NGユーザのリストを返す
     * @return NGユーザリスト
     */
    public List<NGUser> findAll() {
        return tm.required(ngUserDao::findAll);
    }

    /**
     * 受け取ったNGユーザをDBに格納する
     * @param ngUser NGUserインスタンス
     * @return 処理した投稿数
     */
    public int insert(NGUser ngUser) {
        return tm.required(() -> ngUserDao.insert(ngUser));
    }

    /**
     * 受け取ったidのNGユーザをDBから削除する
     * @param id NGユーザid
     * @return 処理した投稿数
     */
    public int delete(int id) {
        return tm.required(() -> ngUserDao.deleteById(id));
    }
}
