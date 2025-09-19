package com.bytefinger.toutuo.common.util;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bytefinger.common.core.utils.CamelUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.security.enums.RoleKeys;
import com.bytefinger.common.security.annotation.DataFillField;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author pat
 * @date 2022/11/03 16:34
 **/
@Getter
@Slf4j
public class TableMataDate {

    private static final Map<Class<?>, TableMataDate> TABLE_CACHE = new ConcurrentHashMap<>(64);

    /**
     * 表名
     */
    private String tableName;

    /**
     * 类
     */
    private Class<?> clazz;

    /**
     * value字段
     */
    private Map<String, Field> valueFieldMap;

    /**
     * 需要处理额字段
     */
    private Map<String, Field> processFieldMap;

    /**
     * 关联字段
     */
    private Map<String, Field> joinFieldMap;

    /**
     * 权限字段
     */
    private Map<String, Field> roleKeyMap;

    public Class<?> getClazz() {
        return this.clazz;
    }

    private TableMataDate(Class<?> clazz) {
        this.clazz = clazz;
        valueFieldMap = Maps.newHashMap();
        processFieldMap = Maps.newLinkedHashMap();
        joinFieldMap = Maps.newHashMap();
        roleKeyMap = Maps.newHashMap();
        initTableInfo(clazz);
    }

    public static TableMataDate forClass(Class<?> entityClass) {
        TableMataDate tableMataDate = TABLE_CACHE.get(entityClass);
        if (tableMataDate == null) {
            tableMataDate = new TableMataDate(entityClass);
            TABLE_CACHE.put(entityClass, tableMataDate);
        }

        return tableMataDate;
    }

    /**
     * 根据注解初始化表信息，
     *
     * @param clazz 实体类的 class
     */
    private void initTableInfo(Class<?> clazz) {
        List<Field> fieldList = Lists.newArrayList();

        Class tempClass = clazz;
        while (tempClass != null) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass();
        }

        tableName = clazz.isAnnotationPresent(TableName.class) ? clazz.getAnnotation(TableName.class).value() : CamelUtils.camelToUndeline(clazz.getSimpleName());
        if (fieldList.isEmpty()) {
            return;
        }

        LinkedHashMap<String, Field> fieldMaps = fieldList.stream().collect(Collectors.toMap(Field::getName, field -> field, (n1, n2) -> n1, LinkedHashMap::new));
        for (Field field : fieldList) {
            if (null != field) {
                DataFillField dataFillField = field.getAnnotation(DataFillField.class);
                if (null != dataFillField) {
                    Field dataField = fieldMaps.get(dataFillField.dataField());
                    if (null != dataField) {
                        PropertyDescriptor dataDescriptor = BeanUtils.getPropertyDescriptor(clazz, dataField.getName());
                        PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(clazz, field.getName());
                        if (null != dataDescriptor && null != dataDescriptor.getReadMethod() && null != descriptor && null != descriptor.getReadMethod()) {
                            valueFieldMap.put(dataField.getName(), dataField);
                            processFieldMap.put(field.getName(), field);

                            // 关联字段
                            Field joinField = fieldMaps.get(dataFillField.joinWrite());
                            if (null != joinField) {
                                PropertyDescriptor joinFieldDescriptor = BeanUtils.getPropertyDescriptor(clazz, joinField.getName());
                                if (null != joinFieldDescriptor && null != joinFieldDescriptor.getReadMethod()) {
                                    joinFieldMap.put(joinField.getName(), joinField);
                                }
                            }

                            // 权限字段
                            for (RoleKeys roleKeys : dataFillField.roleKeysField()) {
                                if (null != roleKeys && StringUtils.isNotNull(roleKeys.getField())) {
                                    Field roleKeysField = fieldMaps.get(roleKeys.getField());
                                    if (null != roleKeysField) {
                                        PropertyDescriptor joinFieldDescriptor = BeanUtils.getPropertyDescriptor(clazz, roleKeysField.getName());
                                        if (null != joinFieldDescriptor && null != joinFieldDescriptor.getReadMethod()) {
                                            roleKeyMap.put(roleKeysField.getName(), roleKeysField);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置属性对应的值
     *
     * @param obj
     * @param field
     * @return
     */
    public Object getFieldValue(Object obj, Field field) {
        if (null != obj) {
            if (null != field) {
                field.setAccessible(true);
                try {
                    return field.get(obj);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage());
                }
            }
        }

        return null;
    }

    /**
     * 设置属性对应的值
     *
     * @param obj
     * @param field
     * @param value
     */
    public void setFieldValue(Object obj, Field field, Object value) {
        if (null != obj && null != value) {
            field.setAccessible(true);
            try {
                field.set(obj, value);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage());
            }
        }
    }
}