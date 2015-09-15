package kotori;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;

public class Redis {
    private static final Redis redis = new Redis();
    private final String DEFAULT_HOST_NAME  = "localhost";
    private JedisPool pool = null;

    private Redis() {
        setRedis();
    }

    public static Redis getRedis() {
        return redis;
    }

    private void setRedis() {
        pool = new JedisPool(new JedisPoolConfig(), getRedisHostname());
    }

    public JedisPool getJedisPool() {
        return pool;
    }

    private String getRedisHostname() {
        ResourceBundle resource = ResourceBundle.getBundle("dbsettings");
        try {
            return Optional.ofNullable(resource.getString("redis_hostname")).orElse(DEFAULT_HOST_NAME);
        } catch (MissingResourceException e) {
            return DEFAULT_HOST_NAME;
        }
    }
}
