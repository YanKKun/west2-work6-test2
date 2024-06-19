package cn.yk.starter.RateLimit;

import cn.yk.starter.enums.LimitType;

import java.lang.annotation.*;
import java.time.temporal.ChronoUnit;

/**
 * @author 12080
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface rateLimiting {
    String key() default "limitRate";

    int second() default 60;

    ChronoUnit timeType() default ChronoUnit.SECONDS;

    int maxTimes() default 100;

    LimitType limitType() default LimitType.Default;
}
