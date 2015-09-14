package kotori;

import javax.sql.DataSource;

import logger.CommonsJdbcLogger;
import org.seasar.doma.SingletonConfig;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.seasar.doma.jdbc.dialect.MysqlDialect;
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource;
import org.seasar.doma.jdbc.tx.LocalTransactionManager;
import org.seasar.doma.jdbc.tx.TransactionManager;

import java.util.ResourceBundle;

@SingletonConfig
public class DBConfig implements Config {

    private static final DBConfig CONFIG = new DBConfig();
    private final Dialect dialect;
    private final LocalTransactionDataSource dataSource;
    private final TransactionManager transactionManager;
    private final JdbcLogger jdbcLogger;

    private DBConfig() {
        dialect = new MysqlDialect();
        ResourceBundle resource = ResourceBundle.getBundle("dbsettings");
        dataSource = new LocalTransactionDataSource(
                resource.getString("mysql_hostname"),
                resource.getString("mysql_username"),
                resource.getString("mysql_password"));
        jdbcLogger = new CommonsJdbcLogger();
        transactionManager = new LocalTransactionManager(
                dataSource.getLocalTransaction(jdbcLogger));
    }


    @Override
    public Dialect getDialect() {
        return dialect;
    }

    @Override
    public JdbcLogger getJdbcLogger() {
        return jdbcLogger;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    public static DBConfig singleton() {
        return CONFIG;
    }
}