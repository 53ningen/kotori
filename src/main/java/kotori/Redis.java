package kotori;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

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
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    public JedisPool getJedisPool() {
        return pool;
    }

}
