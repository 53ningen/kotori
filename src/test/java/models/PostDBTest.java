package models;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import models.posts.PostDB;
import models.posts.PostPayload;
import org.junit.Before;
import org.junit.Test;

public class PostDBTest {
    private PostDB postDB;
    private PostPayload postPayload;

    @Before
    public void setUp() throws Exception {
        postDB = new PostDB();
        postPayload = new PostPayload();
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
        String actual = postDB.getAllPosts().get(0).getContent();
        assertThat(actual, is("hoge"));

    }
}
