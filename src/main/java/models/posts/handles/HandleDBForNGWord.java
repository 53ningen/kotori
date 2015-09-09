package models.posts.handles;

import bulletinBoard.DBConfig;
import databases.daos.NGWordDao;
import databases.entities.NGWord;
import helper.DaoImplHelper;
import models.posts.utils.DBSelectOptions;
import org.seasar.doma.jdbc.SelectOptions;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class HandleDBForNGWord {
    private final NGWordDao ngWordDao = DaoImplHelper.get(NGWordDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    /**
     * NGワードのリストを返す
     * @param options SelectOptions
     * @return NGワードリスト
     */
    public List<NGWord> select(SelectOptions options) {
        return tm.required(() -> ngWordDao.select(options));
    }

    /**
     * NGワードのリストを全件返す
     * @return NGワードリスト
     */
    public List<NGWord> selectAll() {
        return tm.required(() -> ngWordDao.select(SelectOptions.get()));
    }

    /**
     * 受け取ったNGワードをDBに格納する
     * @param ngWord NGWordインスタンス
     * @return 格納されたNGWordインスタンス
     */
    public List<NGWord> insert(NGWord ngWord) {
        return tm.required(() -> {
            int result = ngWordDao.insert(ngWord);
            if (result < 1) {
                return Collections.<NGWord>emptyList();
            }
            return ngWordDao.select(DBSelectOptions.getDBSelectOptions().setOptions(1));
        });
    }

    /**
     * 受け取ったidのNGワードをDBから削除する（Admin用）
     * @param id NGワードid
     * @return 処理した投稿数
     */
    public int delete(int id) {
        return tm.required(() -> ngWordDao.deleteById(id));
    }
}
