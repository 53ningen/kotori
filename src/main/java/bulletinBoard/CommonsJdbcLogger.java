package bulletinBoard;

import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class CommonsJdbcLogger implements JdbcLogger {
    private Logger logger;

    @Override
    /**
     * Daoメソッドの実行開始
     */
    public void logDaoMethodEntering(String callerClassName, String callerMethodName, Object... args) {
        logger = LoggerFactory.getLogger(callerClassName);
        logger.info("START " + callerClassName + "#" + callerMethodName);
    }

    @Override
    /**
     * Daoメソッドの実行終了
     */
    public void logDaoMethodExiting(String callerClassName, String callerMethodName, Object result) {
        logger = LoggerFactory.getLogger(callerClassName);
        logger.info("END " + callerClassName + "#" + callerMethodName);
    }

    @Override
    /**
     * Daoメソッドの例外終了
     */
    public void logDaoMethodThrowing(String callerClassName, String callerMethodName, RuntimeException e) {
        logger = LoggerFactory.getLogger(callerClassName);
        logger.info("END " + callerClassName + "#" + callerMethodName + " RuntimeException: " + e);
    }

    @Override
    /**
     * Daoメソッドの実行スキップ
     */
    public void logSqlExecutionSkipping(String callerClassName, String callerMethodName, SqlExecutionSkipCause cause) {
        logger = LoggerFactory.getLogger(callerClassName);
        logger.info("SKIPPED(" + cause.name() + ")" + callerClassName + "#" + callerMethodName);
    }

    @Override
    /**
     * SQLのログ出力
     */
    public void logSql(String callerClassName, String callerMethodName, Sql<?> sql) {
        logger = LoggerFactory.getLogger(callerClassName);
        String message = String.format("SQL Log. sqlFilePath=[%s],%n%s", sql.getSqlFilePath(), sql.getFormattedSql());
        logger.info(message);
    }

    @Override
    public void logTransactionBegun(String callerClassName, String callerMethodName, String transactionId) {

    }

    @Override
    public void logTransactionEnded(String callerClassName, String callerMethodName, String transactionId) {

    }

    @Override
    public void logTransactionCommitted(String callerClassName, String callerMethodName, String transactionId) {

    }

    @Override
    public void logTransactionSavepointCreated(String callerClassName, String callerMethodName, String transactionId, String savepointName) {

    }

    @Override
    public void logTransactionRolledback(String callerClassName, String callerMethodName, String transactionId) {

    }

    @Override
    public void logTransactionSavepointRolledback(String callerClassName, String callerMethodName, String transactionId, String savepointName) {

    }

    @Override
    public void logTransactionRollbackFailure(String callerClassName, String callerMethodName, String transactionId, SQLException e) {

    }

    @Override
    public void logAutoCommitEnablingFailure(String callerClassName, String callerMethodName, SQLException e) {

    }

    @Override
    public void logTransactionIsolationSettingFailuer(String callerClassName, String callerMethodName, int transactionIsolationLevel, SQLException e) {

    }

    @Override
    public void logConnectionClosingFailure(String callerClassName, String callerMethodName, SQLException e) {

    }

    @Override
    public void logStatementClosingFailure(String callerClassName, String callerMethodName, SQLException e) {

    }

    @Override
    public void logResultSetClosingFailure(String callerClassName, String callerMethodName, SQLException e) {

    }
}
