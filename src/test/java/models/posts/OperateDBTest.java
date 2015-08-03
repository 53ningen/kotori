package models.posts;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import databases.entities.Contribution;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

public class OperateDBTest extends OperateDB {
    private OperateDB operateDB;
    private Payload payload;

    @Before
    public void setUp() throws Exception {
        operateDB = new OperateDB();
        payload = new Payload();
        payload.setUsername("小泉花陽");
        payload.setTitle("fuga");
        payload.setContent("hoge");
    }

    @Test
    public void 受け取ったPayloadがContributionインスタンスとして正しく生成される() throws Exception {
        // exercise
        Optional<Contribution> contribution = operateDB.createContribution(payload);

        // verify
        assertThat(contribution.get().getTitle(), is("fuga"));
        assertThat(contribution.get().getContent(), is("hoge"));
    }
}
