package models.contributions;

import databases.entities.Contribution;
import models.payloads.PostPayload;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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
    public void 受け取ったPayloadがContributionインスタンスとして正しく生成される() throws Exception {
        // exercise
        Optional<Contribution> contributionOpt = handleContribution.createContribution(payload);
        Contribution contribution = contributionOpt.get();

        // verify
        assertThat(contribution.getUsername(), is("小泉花陽"));
        assertThat(contribution.getTitle(), is("fuga"));
        assertThat(contribution.getContent(), is("hoge"));
    }

    @Test
    public void 削除キーがハッシュ化されて保存されている() throws Exception {
        // exercise
        Optional<Contribution> contributionOpt = handleContribution.createContribution(payload);
        Contribution contribution = contributionOpt.get();

        // verify
        String deleteKey = contribution.getDeleteKey();
        assertThat(deleteKey, is(Encryption.getSaltedDeleteKey("pass", "小泉花陽")));
        assertThat(deleteKey, is(not(Encryption.getSaltedDeleteKey("pass", "星空凛"))));
    }

    @Test
    public void 整形された日時が付与されたContributionを返す() throws Exception {
        // setup
        Contribution contribution = new Contribution();
        contribution.setCreatedAt(LocalDateTime.of(2015, 8, 3, 12, 24, 36));
        List<Contribution> contributions = new ArrayList<>();
        contributions.add(contribution);

        // exercise
        Contribution editedContribution = handleContribution.addInformationContributions(contributions).get(0);

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
        List<Contribution> editedContributions = handleContribution.addInformationContributions(contributions);

        // verify
        assertNotNull(editedContributions);
        assertThat(editedContributions.get(0).getIsNew(), is(true));
        assertThat(editedContributions.get(1).getIsNew(), is(false));
        assertThat(editedContributions.get(2).getIsNew(), is(false));
    }

    @Test
    public void unicodeエスケープされた文字列を元に戻す() throws Exception {
        // setup
        String escapeStr = "{\"username\":\"\\\\u897f\\\\u6728\\\\u91ce\\\\u771f\\\\u59eb\", \"content\":\"\\\\u000a\"}";
        Method method = handleContribution.getClass().getDeclaredMethod("unescapeUnicode", String.class);
        method.setAccessible(true);

        // exercise
        String unescapeStr = (String) method.invoke(handleContribution, escapeStr);

        // verify
        assertThat(unescapeStr, is("{\"username\":\"西木野真姫\", \"content\":\"\\\\n\"}"));
    }

    @Test
    public void HTMLタグを除いた文字列を返す() throws Exception {
        // setup
        String str = "{\"content\":\"hoge<b>foo</b>bar\"}";
        Method method = handleContribution.getClass().getDeclaredMethod("validateBody", String.class);
        method.setAccessible(true);

        // exercise
        String excludedStr = (String) method.invoke(handleContribution, str);

        // verify
        assertThat(excludedStr, is("{\"content\":\"hogefoobar\"}"));
    }
}