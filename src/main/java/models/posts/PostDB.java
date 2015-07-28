package models.posts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostDB {
    private int nextId = 1;
    private HashMap<Integer, PostPayload> posts = new HashMap<>();

    /**
     * 受け取った投稿をDBに格納する
     * @param payload
     * @return 投稿ID
     */
    public String createPost(PostPayload payload) {
        int id = nextId++;
        posts.put(id, payload);
        return String.valueOf(id);
    }

    /**
     * 全ての投稿を返す
     * @return
     */
    public List getAllPosts() {
        return posts.keySet().stream().sorted().map((id) -> posts.get(id)).collect(Collectors.toList());
    }

}
