package models.posts.handles;

import bulletinBoard.DBConfig;
import databases.daos.NGWordDao;
import databases.entities.NGWord;
import helper.DaoImplHelper;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.List;

public class HandleDBForNGWord {
    private final NGWordDao ngWordDao = DaoImplHelper.get(NGWordDao.class);
    private final TransactionManager tm = DBConfig.singleton().getTransactionManager();

    /**
     * NGワードのリストを返す
     * @return NGワードリスト
     */
    public List<NGWord> findAll() {
        return tm.required(ngWordDao::findAll);
    }

    /**
     * 受け取ったNGワードをDBに格納する
     * @param ngWord NGWordインスタンス
     * @return 処理した投稿数
     */
    public int insert(NGWord ngWord) {
        return tm.required(() -> ngWordDao.insert(ngWord));
    }

    /**
     * 受け取ったidのNGワードをDBから削除する（Admin用）
     * @param id NGワードid
     * @return 処理した投稿数
     */
    public int deleteById(int id) {
        return tm.required(() -> ngWordDao.deleteById(id));
    }
}
