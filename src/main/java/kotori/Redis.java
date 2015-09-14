package kotori;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Optional;
import java.util.ResourceBundle;

public class Redis {
    private static final Redis redis = new Redis();
    private JedisPool pool = null;

    private Redis() {
        setRedis();
    }

    public static Redis getRedis() {
        return redis;
    }

    private void setRedis() {
        pool = new JedisPool(new JedisPoolConfig(), getRedisHostname().orElse("localhost"));
    }

    public JedisPool getJedisPool() {
        return pool;
    }

    private Optional<String> getRedisHostname() {
        ResourceBundle resource = ResourceBundle.getBundle("dbsettings");
        return Optional.ofNullable(resource.getString("redis_hostname"));
    }
}
