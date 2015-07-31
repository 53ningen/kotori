package helper;

public class DaoImplHelper {
    public static <T> T get(Class<T> daoInterface) {
        String implClassName = daoInterface.getName() + "Impl";
        try {
            Class<T> implClass = cast(Class.forName(implClassName));
            return implClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T cast(Object obj) {
        return (T) obj;
    }
}
