package models.contributions;

import databases.entities.Contribution;
import models.payloads.PostPayload;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class HandleContributionTest {
    private HandleContribution handleContribution;
    private PostPayload payload;

    @Before
    public void setUp() throws Exception {
        handleContribution = new HandleContribution();
        payload = new PostPayload();
        payload.setUsername("小泉花陽");
        payload.setTitle("fuga");
        payload.setContent("hoge");
        payload.setDeleteKey("pass");
    }

    @Test
    public void 整形された日時が付与されたContributionを返す() throws Exception {
        // setup
        Contribution contribution = spy(new Contribution(payload));
        when(contribution.getCreatedAt()).thenReturn(LocalDateTime.of(2015, 8, 3, 12, 24, 36));

        // exercise
        Contribution editedContribution = handleContribution.addInformationContribution(contribution);

        // verify
        assertNotNull(editedContribution);
        assertThat(editedContribution.getEditedCreatedTime(), is("2015/08/03 12:24:36"));
    }

    @Test
    public void 新着投稿であるかどうかをBooleanとして返す() throws Exception {
        // setup
        LocalDateTime now = LocalDateTime.now();
        Contribution contribution = spy(new Contribution(payload));
        List<Contribution> contributions = new ArrayList<>();

        when(contribution.getCreatedAt()).thenReturn(now.minusDays(1).plusMinutes(1)); // 現在より23時間59分前の投稿
        contributions.add(contribution);

        contribution = spy(new Contribution(payload));
        when(contribution.getCreatedAt()).thenReturn(now.minusDays(1)); // 現在より1日前の投稿(24時間前の投稿)
        contributions.add(contribution);

        contribution = spy(new Contribution(payload));
        when(contribution.getCreatedAt()).thenReturn(now.plusDays(1)); // 現在より1日後の投稿(本来は投稿されない)
        contributions.add(contribution);

        // exercise
        List<Contribution> editedContributions = handleContribution.addInformationContributions(contributions);

        // verify
        assertNotNull(editedContributions);
        assertThat(editedContributions.get(0).getIsNew(), is(true));
        assertThat(editedContributions.get(1).getIsNew(), is(false));
        assertThat(editedContributions.get(2).getIsNew(), is(false));
    }
}