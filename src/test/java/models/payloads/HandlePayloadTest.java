package models.payloads;

import databases.entities.NGUser;
import databases.entities.NGWord;
import databases.entities.User;
import databases.resources.DBNGUserResource;
import databases.resources.DBNGWordResource;
import models.handles.HandleDB;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class HandlePayloadTest {
    @Rule
    public final DBNGWordResource ngWordResource = new DBNGWordResource();
    @Rule
    public final DBNGUserResource ngUserResource = new DBNGUserResource();

    @Test
    public void PayloadがNGワードを含んでいなければtrueを返す() throws Exception {
        // setup
        PostPayload payload = new PostPayload();
        payload.setTitle("foo");
        payload.setContent("テスト");
        List<NGWord> ngWords = HandleDB.ngWord().selectAll();

        // exercise
        Boolean stu = HandlePayload.isValidContent(ngWords, payload);

        // verify
        assertTrue(stu);
    }

    @Test
    public void PayloadがNGワードを含んでいればfalseを返す() throws Exception {
        // setup
        PostPayload payload = new PostPayload();
        payload.setTitle("テスト");
        payload.setContent("hoge");
        List<NGWord> ngWords = HandleDB.ngWord().selectAll();

        // exercise
        Boolean stu = HandlePayload.isValidContent(ngWords, payload);

        // verify
        assertFalse(stu);
    }

    @Test
    public void UserがNGユーザでなければtrueを返す() throws Exception {
        // setup
        User user = new User("hoge", "");
        List<NGUser> ngUsers = HandleDB.ngUser().selectAll();

        // exercise
        Boolean stu = HandlePayload.isValidUser(ngUsers, Optional.of(user));

        // verify
        assertTrue(stu);
    }

    @Test
    public void UserがNGユーザであればfalseを返す() throws Exception {
        // setup
        User user = new User("hogehoge", "");
        List<NGUser> ngUsers = HandleDB.ngUser().selectAll();

        // exercise
        Boolean stu = HandlePayload.isValidUser(ngUsers, Optional.of(user));

        // verify
        assertFalse(stu);
    }

    @Test
    public void unicodeエスケープされた文字列を元に戻す() throws Exception {
        // setup
        String escapeStr = "{\"username\":\"\\\\u897f\\\\u6728\\\\u91ce\\\\u771f\\\\u59eb\", \"content\":\"\\\\u000a\"}";

        // exercise
        String unescapeStr = HandlePayload.unescapeUnicode(escapeStr);

        // verify
        assertThat(unescapeStr, is("{\"username\":\"西木野真姫\", \"content\":\"\\\\n\"}"));
    }

    @Test
    public void HTMLタグを除いた文字列を返す() throws Exception {
        // setup
        String str = "{\"content\":\"hoge<b>foo</b>bar\"}";
        Method method = HandlePayload.class.getDeclaredMethod("validateBody", String.class);
        method.setAccessible(true);

        // exercise
        String excludedStr = (String) method.invoke(HandlePayload.class, str);

        // verify
        assertThat(excludedStr, is("{\"content\":\"hogefoobar\"}"));
    }
}