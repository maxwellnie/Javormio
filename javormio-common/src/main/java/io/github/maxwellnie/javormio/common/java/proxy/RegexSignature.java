package io.github.maxwellnie.javormio.common.java.proxy;

import io.github.maxwellnie.javormio.common.java.api.Matcher;

import java.lang.reflect.Method;

/**
 * The signature of method.It's support regex match.
 *
 * @author Maxwell Nie
 */
public class RegexSignature implements Matcher<Method> {
    private final String regex;

    public RegexSignature(String regex) {
        this.regex = regex;
    }

    /**
     * Created a RegexSignature which match all methods.
     *
     * @return RegexSignature
     */
    public static RegexSignature allMethods() {
        return new RegexSignature(".*");
    }

    /**
     * Created a RegexSignature which match methods start with regex.
     *
     * @param regex
     * @return RegexSignature
     */
    public static RegexSignature startWith(String regex) {
        return new RegexSignature(regex + ".*");
    }

    /**
     * Created a RegexSignature which match methods end with regex.
     *
     * @param regex
     * @return RegexSignature
     */
    public static RegexSignature endWith(String regex) {
        return new RegexSignature(".*" + regex);
    }

    @Override
    public boolean matches(Method method) {
        String methodName = method.getName();
        return methodName.matches(regex);
    }
}
