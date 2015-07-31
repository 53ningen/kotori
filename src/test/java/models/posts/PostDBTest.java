package models.posts;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import databases.entities.Contribution;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class PostDBTest extends PostDB {
    private PostDB postDB;
    private PostPayload postPayload;

    @Before
    public void setUp() throws Exception {
        postDB = new PostDB();
        postPayload = new PostPayload();
        postPayload.setTitle("fuga");
        postPayload.setContent("hoge");
    }

    @Test
    public void 受け取ったPayloadがContributionインスタンスとして正しく生成される() throws Exception {
        // exercise
        Optional<Contribution> contribution = postDB.createContribution(postPayload);

        // verify
        assertThat(contribution.get().getTitle(), is("fuga"));
        assertThat(contribution.get().getContent(), is("hoge"));
    }
}
