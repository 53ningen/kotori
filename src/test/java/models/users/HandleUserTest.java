package models.users;

import databases.entities.User;
import databases.resources.DBAutoLoginResource;
import databases.resources.DBUserResource;
import helper.RequestHelper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import spark.Request;

import java.util.Optional;

import static models.users.HandleUser.createUser;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class HandleUserTest {
    @Rule
    public final DBAutoLoginResource autoLoginResource = new DBAutoLoginResource();
    @Rule
    public final DBUserResource userResource = new DBUserResource();
    private final String AUTH_TOKEN = "auth_token";
    private Request request;

    @Before
    public void setUp() throws Exception {
        request = RequestHelper.Requestモックの生成();
    }

    @Test
    public void Userインスタンスが正しく生成される() throws Exception {
        // setup
        when(request.cookie(AUTH_TOKEN)).thenReturn("testuser_testtoken");

        // exercise
        Optional<User> userOpt = createUser(request);
        User user = userOpt.get();

        // verify
        assertThat(user.getUserid(), is("testuser"));
        assertThat(user.getUsername(), is("テストユーザ"));
    }

    @Test
    public void ユーザが存在しない場合nullがOptional型にラップされて返る() throws Exception {
        // setup
        when(request.cookie(AUTH_TOKEN)).thenReturn("hoge");

        // exercise
        Optional<User> userOpt = createUser(request);

        // verify
        assertThat(userOpt.isPresent(), is(false));
    }
}
