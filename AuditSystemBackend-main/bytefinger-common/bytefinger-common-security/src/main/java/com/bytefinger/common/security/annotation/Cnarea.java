package com.bytefinger.common.security.annotation;

import com.bytefinger.common.security.enums.CnareaEnum;
import com.bytefinger.common.security.utils.CnareaSerializer;
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
@JsonSerialize(using = CnareaSerializer.class)
public @interface Cnarea {

    /**
     * type
     * 1 = 省份
     * 2 = 市
     * 3 = 区
     * 4 = 街道
     *
     * @return
     */
    CnareaEnum type();

}