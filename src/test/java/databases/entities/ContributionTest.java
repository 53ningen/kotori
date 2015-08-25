package databases.entities;

import models.posts.utils.Encryption;
import models.payloads.PostPayload;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

public class ContributionTest {
    private PostPayload payload;

    @Before
    public void setUp() throws Exception {
        payload = new PostPayload();
        payload.setUsername("小泉花陽");
        payload.setTitle("fuga");
        payload.setContent("hoge");
        payload.setDeleteKey("pass");
    }

    @Test
    public void 受け取ったPayloadがContributionインスタンスとして正しく生成される() throws Exception {
        // exercise
        Contribution contribution = new Contribution(payload);

        // verify
        assertThat(contribution.getUsername(), is("小泉花陽"));
        assertThat(contribution.getTitle(), is("fuga"));
        assertThat(contribution.getContent(), is("hoge"));
    }



    @Test
    public void 削除キーがハッシュ化されて保存されている() throws Exception {
        // exercise
        Contribution contribution = new Contribution(payload);

        // verify
        String deleteKey = contribution.getDeleteKey();
        assertThat(deleteKey, is(Encryption.getSaltedKey("pass", "小泉花陽")));
        assertThat(deleteKey, is(not(Encryption.getSaltedKey("pass", "星空凛"))));
    }
}
