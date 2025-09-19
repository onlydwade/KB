package com.bytefinger.common.das.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.utils.ip.IpUtils;
import com.bytefinger.common.security.utils.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Mybatis Plus Config
 *
 * @author pat
 * @date 2022/10/06 12:59
 **/
@Component
public class FillDataHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.fill(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.fill(new int[]{2, 4, 6, 8}, metaObject);
    }

    private static final Map<Integer, String[]> KEY_MAP = new HashMap<>();

    static {
        KEY_MAP.put(1, new String[]{"createTime"});
        KEY_MAP.put(2, new String[]{"updateTime"});

        KEY_MAP.put(3, new String[]{"createUserId",});
        KEY_MAP.put(4, new String[]{"updateUserId"});

        KEY_MAP.put(5, new String[]{"creaateUserName"});
        KEY_MAP.put(6, new String[]{"updateUserName"});

        KEY_MAP.put(7, new String[]{});
        KEY_MAP.put(8, new String[]{"operIp"});
    }

    private void fill(int[] keys, MetaObject metaObject) {
        for (int key : keys) {
            final String[] fields = KEY_MAP.get(key);
            for (final String field : fields) {
                if (metaObject.hasSetter(field)) {
                    switch (key) {
                        case 1:
                        case 2:
                            this.setFieldValByName(field, new Date(), metaObject);
                            break;
                        case 3:
                        case 4:
                            this.setFieldValByName(field, SecurityUtils.getUserId(), metaObject);
                            break;
                        case 5:
                        case 6:
                            this.setFieldValByName(field, SecurityUtils.getRealname(), metaObject);
                            break;
                        case 7:
                            break;
                        case 8:
                            this.setFieldValByName(field, IpUtils.getIpAddr(ServletUtils.getRequest()), metaObject);
                            break;
                    }
                }
            }
        }
    }

}
