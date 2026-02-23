package util;

public class OutputUtil {
    public static String valueOrDash(Object obj) {
        return obj == null ? " - " : obj.toString();
    }
}
