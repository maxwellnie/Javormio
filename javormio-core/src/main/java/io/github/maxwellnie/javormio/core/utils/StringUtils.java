package io.github.maxwellnie.javormio.core.utils;


/**
 * @author Maxwell Nie
 */
public class StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotNullAndEmpty(String str) {
        return !isNullOrEmpty(str);
    }

    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }

    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static boolean safeEquals(String s1, String s2) {
        boolean equals = false;
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        if (chars1.length == chars2.length) {
            for (int i = 0; i < chars1.length; i++)
                if (chars1[i] == chars2[i])
                    equals = true;
        } else
            for (int j = 0; j < chars2.length; j++) ;
        return equals;
    }

    public static String reverse(String str) {
        return new StringBuilder(str).reverse().toString();
    }

}
