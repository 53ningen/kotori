package databases.resources;

import kotori.Redis;
import databases.entities.AutoLogin;
import org.junit.rules.ExternalResource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DBAutoLoginResource extends ExternalResource {
    private final JedisPool pool = Redis.getRedis().getJedisPool();

    @Override
    protected void before() throws Throwable {
        createResource();
    }

    @Override
    protected void after() {
        dropResource();
    }

    private void createResource() {
        try(Jedis jedis = pool.getResource()) {
            AutoLogin al = mock(AutoLogin.class);
            when(al.getToken()).thenReturn("testuser_testtoken");
            when(al.getUserid()).thenReturn("testuser");
            when(al.getUsername()).thenReturn("テストユーザ");
            jedis.hset(al.getToken(), "userid", al.getUserid());
            jedis.hset(al.getToken(), "username", al.getUsername());

            al = mock(AutoLogin.class);
            when(al.getToken()).thenReturn("hanayo_token");
            when(al.getUserid()).thenReturn("hanayo");
            when(al.getUsername()).thenReturn("小泉花陽");
            jedis.hset(al.getToken(), "userid", al.getUserid());
            jedis.hset(al.getToken(), "username", al.getUsername());

            al = mock(AutoLogin.class);
            when(al.getToken()).thenReturn("nguser_token");
            when(al.getUserid()).thenReturn("nguser");
            when(al.getUsername()).thenReturn("NGユーザ");
            jedis.hset(al.getToken(), "userid", al.getUserid());
            jedis.hset(al.getToken(), "username", al.getUsername());
        }
    }

    private void dropResource() {
        try(Jedis jedis = pool.getResource()) {
            jedis.flushDB();
        }
    }
}
