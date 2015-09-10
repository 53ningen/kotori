package models.handles;

import bulletinBoard.DBConfig;
import databases.daos.NGUserDao;
import databases.entities.NGUser;
import helper.DaoImplHelper;
import models.utils.DBSelectOptions;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.Collections;
import java.util.List;

public class HandleDBForNGUser {
    private final NGUserDao ngUserDao = DaoImplHelper.get(NGUserDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    /**
     * NGワードのリストを返す
     * @param options SelectOptions
     * @return NGワードリスト
     */
    public List<NGUser> select(SelectOptions options) {
        return tm.required(() -> ngUserDao.select(options));
    }

    /**
     * NGユーザのリストを全件返す
     * @return NGユーザリスト
     */
    public List<NGUser> selectAll() {
        return tm.required(() -> ngUserDao.select(SelectOptions.get()));
    }

    /**
     * 受け取ったNGユーザをDBに格納する
     * @param ngUser NGUserインスタンス
     * @return 格納されたNGUserインスタンス
     */
    public List<NGUser> insert(NGUser ngUser) {
        return tm.required(() -> {
            int result = ngUserDao.insert(ngUser);
            if (result < 1) {
                return Collections.<NGUser>emptyList();
            }
            return ngUserDao.select(DBSelectOptions.getDBSelectOptions().setOptions(1));
        });
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
