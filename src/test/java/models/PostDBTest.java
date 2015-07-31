package models;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import models.posts.PostDB;
import models.posts.PostInfo;
import models.posts.PostPayload;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

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
    public void 受け取った投稿をDBに格納しID1を返す() throws Exception {
        // exercise
        String id = postDB.createPost(postPayload);

        // verify
        assertThat(id, is("1"));
    }

    @Test
    public void 投稿idがインクリメントされる() throws Exception {
        // exercise
        String id1 = postDB.createPost(postPayload);
        String id2 = postDB.createPost(postPayload);

        // verify
        assertThat(id1, is("1"));
        assertThat(id2, is("2"));
    }

    @Test
    public void 受け取った投稿がDBに格納されている() throws Exception {
        // exercise
        postDB.createPost(postPayload);

        // verify
        PostInfo actual = postDB.getAllPosts().get(0);
        assertThat(actual.getId(), is(1));
        assertThat(actual.getTitle(), is("fuga"));
        assertThat(actual.getContent(), is("hoge"));
    }

    @Test
    public void 投稿した日付が整形されてDBに格納されている() throws Exception {
        // setup
        PostCalender sut = new PostCalender(newCalender(2015, 0, 1, 12, 0));

        // verify
        assertThat(sut.getPostDate(), is("2015/01/01 12:00"));
    }

    /**
     * テストする日付を設定する
     * @param year
     * @param month 0から始まる
     * @param day
     * @param hour
     * @param minute
     * @return Calenderオブジェクト
     */
    private Calendar newCalender(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute);
        return cal;
    }
}
