package models.handles;

import kotori.Redis;
import databases.entities.AutoLogin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.Optional;

public class HandleDBForAutoLogin extends HandleDB {
    private Jedis jedis;
    private final JedisPool pool = Redis.getRedis().getJedisPool();

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
     * KeyとFieldから対応するValueを取得する
     * @param token トークン
     * @return DBに存在していればvalueを返す
     */
    public Optional<String> select(String token, String field) {
        try {
            jedis = pool.getResource();
            return Optional.ofNullable(jedis.hget(token, field));
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
                Long result = jedis.hset(key, "userid", al.getUserid());
                if (result < 1L) {
                    return 0L;
                }
                result = jedis.hset(key, "username", al.getUsername());
                jedis.expire(key, 60 * 60 * 24 * 7);
                return result;
            }
        } catch (JedisDataException e) {
            return 0L;
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
