package com.bytefinger.common.security.annotation;

import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.common.security.enums.RoleKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author pat
 * @date 2022/11/03 22:45
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataFillField {

    /**
     * 数据字段
     *
     * @return
     */
    String dataField();

    /**
     * 处理类型
     *
     * @return
     */
    FillMethod fillMethod();

    /**
     * 关联回写字段
     *
     * @return
     */
    String joinWrite() default "";

    /**
     * 数据权限
     *
     * @return
     */
    RoleKeys[] roleKeysField() default {};

    /**
     * 别名
     *
     * @return
     */
    String aliasName() default "";
}