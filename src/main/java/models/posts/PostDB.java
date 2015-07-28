package models.posts;

import static java.util.Comparator.*;

import java.util.Calendar;
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
        PostCalender postCalender = new PostCalender();
        PostInfo postInfo = new PostInfo();
        postInfo.setId(id);
        postInfo.setDate(postCalender.getPostDate());
        postInfo.setTitle(payload.getTitle());
        postInfo.setContent(payload.getContent());
        posts.put(id, postInfo);
        return String.valueOf(id);
    }

    /**
     * 全ての投稿をID降順で返す
     * @return 投稿リスト
     */
    public List<PostInfo> getAllPosts() {
        return posts.keySet().stream().sorted(reverseOrder()).map(posts::get).collect(Collectors.toList());
    }


    /**
     * 投稿日時を管理するネストトップクラス
     */
    protected static class PostCalender {
        private final Calendar cal;

        public PostCalender() {
            this.cal = Calendar.getInstance();
        }

        public PostCalender(Calendar cal) {
            this.cal = cal;
        }

        /**
         * 現在の日時を返す
         * @return "yyyy/mm/dd hh:mm"形式の日時
         */
        public String getPostDate() {
            int year = cal.get(Calendar.YEAR);
            String month = convertPostDateStr(cal.get(Calendar.MONTH) + 1);
            String day = convertPostDateStr(cal.get(Calendar.DATE));
            String hour = convertPostDateStr(cal.get(Calendar.HOUR_OF_DAY));
            String minute = convertPostDateStr(cal.get(Calendar.MINUTE));

            return year + "/" + month + "/" + day + " " + hour + ":" + minute;
        }

        /**
         * 日付を整形して返す
         * @param date
         * @return 整形済みのString
         */
        private String convertPostDateStr(int date) {
            return date < 10 ? "0" + date : String.valueOf(date);
        }

    }
}
