package models.posts;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import databases.entities.Contribution;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
        Optional<Contribution> contributionOpt = operateDB.createContribution(payload);
        Contribution contribution = contributionOpt.get();

        // verify
        assertThat(contribution.getUsername(), is("小泉花陽"));
        assertThat(contribution.getTitle(), is("fuga"));
        assertThat(contribution.getContent(), is("hoge"));
    }

    @Test
    public void 整形された日時が付与されたContributionを返す() throws Exception {
        // setup
        Contribution contribution = new Contribution();
        contribution.setCreatedAt(LocalDateTime.of(2015, 8, 3, 12, 24, 36));
        List<Contribution> contributions = new ArrayList<>();
        contributions.add(contribution);

        // exercise
        Contribution editedContribution = operateDB.addInformationContributions(contributions).get(0);

        // verify
        assertNotNull(editedContribution);
        assertThat(editedContribution.getEditedCreatedTime(), is("2015/08/03 12:24:36"));
    }

    @Test
    public void 新着投稿であるかどうかをBooleanとして返す() throws Exception {
        // setup
        LocalDateTime now = LocalDateTime.now();
        Contribution contribution = new Contribution();
        List<Contribution> contributions = new ArrayList<>();

        contribution.setCreatedAt(now.minusDays(1).plusMinutes(1)); // 現在より23時間59分前の投稿
        contributions.add(contribution);

        contribution = new Contribution();
        contribution.setCreatedAt(now.minusDays(1)); // 現在より1日前の投稿(24時間前の投稿)
        contributions.add(contribution);

        contribution = new Contribution();
        contribution.setCreatedAt(now.plusDays(1)); // 現在より1日後の投稿(本来は投稿されない)
        contributions.add(contribution);

        // exercise
        List<Contribution> editedContributions = operateDB.addInformationContributions(contributions);

        // verify
        assertNotNull(editedContributions);
        assertThat(editedContributions.get(0).getIsNew(), is(true));
        assertThat(editedContributions.get(1).getIsNew(), is(false));
        assertThat(editedContributions.get(2).getIsNew(), is(false));
    }
}
