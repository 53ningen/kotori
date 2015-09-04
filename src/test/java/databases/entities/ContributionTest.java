package databases.entities;

import models.payloads.PostPayload;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ContributionTest {
    private PostPayload payload;
    private User user;

    @Before
    public void setUp() throws Exception {
        payload = new PostPayload();
        payload.setTitle("fuga");
        payload.setContent("hoge");
        user = new User("hanayo", "小泉花陽");
    }

    @Test
    public void 受け取ったPayloadがContributionインスタンスとして正しく生成される() throws Exception {
        // exercise
        Contribution contribution = new Contribution(payload, user);

        // verify
        assertThat(contribution.getUsername(), is("小泉花陽"));
        assertThat(contribution.getTitle(), is("fuga"));
        assertThat(contribution.getContent(), is("hoge"));
    }
}
