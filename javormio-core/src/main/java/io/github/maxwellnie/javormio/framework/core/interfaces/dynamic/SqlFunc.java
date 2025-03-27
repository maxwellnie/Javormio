package io.github.maxwellnie.javormio.framework.core.interfaces.dynamic;

/**
 * @author Maxwell Nie
 */
@FunctionalInterface
public interface SqlFunc<T, R> {
    String modify(SerializableFunction<T, R> getter);
    //User.columns("user_id, user_name")
    //      .where()
    //      .append(notNull(User::getUserId, userId->eq(userId)))
    //      .ok();
    //User.columns()
    //    .filter(notNull(User::getUserId))
    //    .values()
    //    .filter("notNull", "userId")
    //    .ok();
}
