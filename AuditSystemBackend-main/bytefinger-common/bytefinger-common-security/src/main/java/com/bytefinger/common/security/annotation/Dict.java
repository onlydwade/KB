package com.bytefinger.common.security.annotation;

import com.bytefinger.common.security.utils.DictSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pat
 * @date 2022/10/22 02:16
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DictSerializer.class)
public @interface Dict {

    /**
     * 字典类型
     */
    String type() default "";

    /**
     * 父级业态
     */
    String ptype() default "";

}