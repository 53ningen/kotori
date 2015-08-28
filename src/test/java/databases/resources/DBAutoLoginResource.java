package databases.resources;

import bulletinBoard.RedisServer;
import databases.entities.AutoLogin;
import org.junit.rules.ExternalResource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DBAutoLoginResource extends ExternalResource {
    private final JedisPool pool = RedisServer.getRedisServer().getJedisPool();

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
            jedis.set(al.getToken(), al.getUserid());
        }
    }

    private void dropResource() {
        try(Jedis jedis = pool.getResource()) {
            jedis.flushDB();
        }
    }
}
