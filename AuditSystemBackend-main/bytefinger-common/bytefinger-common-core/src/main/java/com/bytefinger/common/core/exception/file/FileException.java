package com.bytefinger.common.core.exception.file;

import com.bytefinger.common.core.exception.base.BaseException;

/**
 * 文件信息异常类
 *
 * @author patrick
 */
public class FileException extends BaseException {
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args) {
        super("file", code, args, null);
    }

}
