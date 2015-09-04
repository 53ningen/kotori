package bulletinBoard;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisServer {
    private static final RedisServer redisServer = new RedisServer();
    private JedisPool pool = null;

    private RedisServer() {
        setRedisServer();
    }

    public static RedisServer getRedisServer() {
        return redisServer;
    }

    private void setRedisServer() {
        pool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    public JedisPool getJedisPool() {
        return pool;
    }

}
