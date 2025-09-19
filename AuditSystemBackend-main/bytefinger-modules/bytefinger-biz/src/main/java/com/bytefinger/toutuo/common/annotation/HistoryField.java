package com.bytefinger.toutuo.common.annotation;

import java.lang.annotation.*;

/**
 * @author pat
 * @date 2022/10/11 22:04
 **/
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HistoryField {

    String value() default "";

}
