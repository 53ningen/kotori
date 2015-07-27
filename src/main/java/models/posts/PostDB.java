package models.posts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PostDB {
    private int nextId = 1;
    private Map posts = new HashMap<>();

    /**
     * 受け取った投稿をDBに格納する
     * @param payload
     * @return 投稿ID
     */
    public String createPost(PostPayload payload) {
        try {
            int id = nextId++;
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(payload);
            posts.put(id, json);
            return String.valueOf(id);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException from a writeValueAsString?");
        }
    }

    /**
     * 全ての投稿を返す
     * @return
     */
    public List getAllPosts() {
        return (List) posts.keySet().stream().sorted().map((id) -> posts.get(id)).collect(Collectors.toList());
    }

}
