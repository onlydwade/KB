package com.bytefinger.common.core.service;


import com.bytefinger.common.core.utils.SpringUtils;

/**
 * 获取当前
 * <p>
 * 用户避开spring 内部调用 事物、AOP不生效的问题
 *
 * @author pat
 * @date 2022/11/19 22:24
 **/
public interface ThisService<T> {

    /**
     * 获取
     *
     * @return
     */
    default T getThis() {
        return (T) SpringUtils.getBean(this.getClass());
    }
}
