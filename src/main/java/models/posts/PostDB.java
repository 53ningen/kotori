package models.posts;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PostDB {
    private static PostDB postDB = new PostDB();
    private int nextId = 1;
    private HashMap<Integer, PostInfo> posts = new HashMap<>();

    public static PostDB getPostDB() {
        return postDB;
    }

    /**
     * 受け取った投稿をDBに格納する
     * @param payload
     * @return 投稿ID
     */
    public String createPost(PostPayload payload) {
        int id = nextId++;
        PostInfo postInfo = new PostInfo();
        postInfo.setId(id);
        postInfo.setContent(payload.getContent());
        posts.put(id, postInfo);
        return String.valueOf(id);
    }

    /**
     * 全ての投稿を返す
     * @return
     */
    public List<PostInfo> getAllPosts() {
        return posts.keySet().stream().sorted().map((id) -> posts.get(id)).collect(Collectors.toList());
    }

}
