package me.noroutine.ucando.cache;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface IfNoneMatch {
    /**
     * TODO: SpEL expression to calculate ETag value
     * @return ETag for request
     */
    String value() default "";
}
