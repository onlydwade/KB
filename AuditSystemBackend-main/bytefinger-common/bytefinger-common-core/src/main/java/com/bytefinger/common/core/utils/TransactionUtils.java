package com.bytefinger.common.core.utils;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 事务工具类
 *
 * @author patrick
 * @date 2019-04-04 16:44
 */
public class TransactionUtils {

    /**
     * 当前事务执行完毕，执行回调
     *
     * @param callback
     */
    public static void afterCommitCallback(Callback callback) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                callback.call();
            }
        });
    }
}
