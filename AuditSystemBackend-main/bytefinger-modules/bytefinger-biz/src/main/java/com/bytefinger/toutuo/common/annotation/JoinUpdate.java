package com.bytefinger.toutuo.common.annotation;

import com.bytefinger.toutuo.common.enums.BizUpdateType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 关联更新
 *
 * @author pat
 * @date 2022/11/03 22:45
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface JoinUpdate {

    BizUpdateType[] values() default {};

}