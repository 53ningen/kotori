package models.posts.handles;

import bulletinBoard.RedisServer;
import databases.entities.AutoLogin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class HandleDBForAutoLogin extends HandleDB {
    private Jedis jedis;
    private final JedisPool pool = RedisServer.getRedisServer().getJedisPool();

    /**
     * AutoLogin情報がDBに存在するか確認する
     * @param token トークン
     * @return DBに存在していればtrueを返す
     */
    public boolean existToken(String token) {
        try {
            jedis = pool.getResource();
            return jedis.exists(token);
        } finally {
            if (jedis != null) jedis.close();
        }
    }

    /**
     * KeyからValueを取得する
     * @param token トークン
     * @return DBに存在していればユーザ名が返る
     */
    public String select(String token) {
        try {
            jedis = pool.getResource();
            return jedis.get(token);
        } finally {
            if (jedis != null) jedis.close();
        }
    }

    /**
     * AutoLogin情報をDBに格納する
     * @param al AutoLoginインスタンス
     * @return 成功時または既にメンバが存在している場合は1を返す
     */
    public Long insert(AutoLogin al) {
        try {
            jedis = pool.getResource();
            String key = al.getToken();
            if (jedis.exists(key)) {
                return 1L;
            } else {
                String result = jedis.set(key, al.getUserid());
                jedis.expire(key, 60 * 60 * 24 * 7);
                return result.isEmpty() ? 0L : 1L;
            }
        } finally {
            if (jedis != null) jedis.close();
        }
    }

    /**
     * AutoLogin情報をDBから削除する
     * @param token トークン
     * @return 成功時は1を、メンバが存在しない場合は0を返す
     */
    public Long delete(String token) {
        try {
            jedis = pool.getResource();
            return jedis.del(token);
        } finally {
            if (jedis != null) jedis.close();
        }
    }
}
