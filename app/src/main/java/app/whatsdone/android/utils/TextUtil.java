package app.whatsdone.android.utils;

import java.util.Arrays;
import java.util.List;

public class TextUtil {

    public static boolean isNullOrEmpty(String string){
        return string == null || string.isEmpty();
    }

    public static List<String> toList(String string){
        return Arrays.asList(string);
    }
}
