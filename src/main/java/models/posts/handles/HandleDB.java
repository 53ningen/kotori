package models.posts.handles;

public class HandleDB {
    private static final HandleDBForContribution handleDBForContribution = new HandleDBForContribution();
    private static final HandleDBForNGUser handleDBForNGUser = new HandleDBForNGUser();
    private static final HandleDBForNGWord handleDBForNGWord = new HandleDBForNGWord();
    private static final HandleDBForUser handleDBForUser = new HandleDBForUser();

    public static HandleDBForContribution contribution() {
        return handleDBForContribution;
    }

    public static HandleDBForNGUser ngUser() {
        return handleDBForNGUser;
    }

    public static HandleDBForNGWord ngWord() {
        return handleDBForNGWord;
    }

    public static HandleDBForUser user() {
        return handleDBForUser;
    }
}
