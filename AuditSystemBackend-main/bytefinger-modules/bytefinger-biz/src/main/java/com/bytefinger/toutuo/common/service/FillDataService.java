package com.bytefinger.toutuo.common.service;

import cn.hutool.core.collection.CollUtil;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.utils.CamelUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.utils.bean.BeanUtils;
import com.bytefinger.common.core.web.domain.vo.CommonNameVO;
import com.bytefinger.common.core.web.domain.vo.DeptVO;
import com.bytefinger.common.core.web.domain.vo.PostVO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.DataFillBean;
import com.bytefinger.common.security.annotation.DataFillField;
import com.bytefinger.common.security.auth.AuthUtil;
import com.bytefinger.common.security.enums.FillMethod;
import com.bytefinger.common.security.enums.RoleKeys;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.user.service.UserService;
import com.bytefinger.toutuo.common.domain.BizUserBaseEntity;
import com.bytefinger.toutuo.common.enums.DataRole;
import com.bytefinger.toutuo.common.mapper.CommonMapper;
import com.bytefinger.toutuo.common.util.TableMataDate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据填充模块
 *
 * @author pat
 * @date 2022/10/19 13:16
 **/
@Service
@AllArgsConstructor
@Slf4j
public class FillDataService {

    private final UserService userService;

    private final CommonMapper commonMapper;

    /**
     * 填充用户信息
     * 循环获取用户id
     * 批量查询，反射填充
     *
     * @param dataList
     */
    public void filler(List<? extends BaseEntity> dataList) {
        if (null == dataList || dataList.isEmpty()) {
            return;
        }

        Class<? extends BaseEntity> beanClass = dataList.get(0).getClass();
        TableMataDate tableMataDate = TableMataDate.forClass(beanClass);

        Map<String, Field> processFieldMap = tableMataDate.getProcessFieldMap();
        Map<String, Field> valueFieldMap = tableMataDate.getValueFieldMap();
        if (CollUtil.isEmpty(processFieldMap) || CollUtil.isEmpty(valueFieldMap)) {
            return;
        }

        Map<FillMethod, List<ProcessField>> processMap = Maps.newLinkedHashMap();
        processClazz(processFieldMap, valueFieldMap, dataList, tableMataDate, processMap);

        if (!processMap.isEmpty()) {
            this.filldata(processMap, dataList, tableMataDate);
        }
    }

    /**
     * 循环 class 结构
     *
     * @param processFieldMap
     * @param valueFieldMap
     * @param tableMataDate
     * @return
     */
    private void processClazz(Map<String, Field> processFieldMap, Map<String, Field> valueFieldMap, List<? extends BaseEntity> dataList, TableMataDate tableMataDate, Map<FillMethod, List<ProcessField>> processMap) {
        // 循环需要处理的字段
        for (Field field : processFieldMap.values()) {
            DataFillField dataFillField = field.getAnnotation(DataFillField.class);
            if (null != dataFillField && valueFieldMap.containsKey(dataFillField.dataField())) {
                if (!processMap.containsKey(dataFillField.fillMethod())) {
                    processMap.put(dataFillField.fillMethod(), Lists.newArrayList());
                }

                ProcessField processField = new ProcessField();
                processField.setFillFiled(field);
                processField.setFillValueFiled(valueFieldMap.get(dataFillField.dataField()));
                processMap.get(dataFillField.fillMethod()).add(processField);
                processField.setAlias(dataFillField.aliasName());

                // 关联写入的字段
                if (StringUtils.isNotNull(dataFillField.joinWrite()) && tableMataDate.getJoinFieldMap().containsKey(dataFillField.joinWrite())) {
                    processField.setJoinField(tableMataDate.getJoinFieldMap().get(dataFillField.joinWrite()));
                }

                // 权限关联字段
                RoleKeys[] roleKeys = dataFillField.roleKeysField();
                if (null != roleKeys && roleKeys.length > 0) {
                    Map<RoleKeys, Field> roleMap = Maps.newHashMap();
                    for (RoleKeys roleKey : roleKeys) {
                        if (tableMataDate.getRoleKeyMap().containsKey(roleKey.getField())) {
                            roleMap.put(roleKey, tableMataDate.getRoleKeyMap().get(roleKey.getField()));
                        }
                    }
                    processField.setRoleKeysFields(roleMap);
                }

                // 批量查询的id值
                Set<Long> ids = Sets.newHashSet();
                for (BaseEntity data : dataList) {
                    Object dataFieldValue = tableMataDate.getFieldValue(data, processField.getFillValueFiled());
                    if (null != dataFieldValue && dataFieldValue instanceof Long) {
                        ids.add(((Long) dataFieldValue));
                    }
                }
                processField.setIds(ids);
            }
        }
    }

    /**
     * 循环查询值，填充对象
     *
     * @param processMap
     * @param dataList
     * @param tableMataDate
     */
    private void filldata(Map<FillMethod, List<ProcessField>> processMap, List<? extends BaseEntity> dataList, TableMataDate tableMataDate) {
        DataFillBean beanAnnotation = tableMataDate.getClazz().getAnnotation(DataFillBean.class);
        String beanName = tableMataDate.getClazz().getSimpleName();
        if (null != beanAnnotation && StringUtils.isNotEmpty(beanAnnotation.name())) {
            beanName = beanAnnotation.name();
        }

        for (FillMethod fillMethod : processMap.keySet()) {
            List<ProcessField> processFields = processMap.get(fillMethod);
            if (CollUtil.isEmpty(processFields)) {
                continue;
            }

            for (ProcessField processField : processFields) {
                if (CollUtil.isNotEmpty(processField.getIds())) {
                    switch (fillMethod) {
                        case USER:
                            this.fillUser(processField, dataList, tableMataDate);
                            break;
                        case USERS:
                            this.fillUsers(processField, dataList, tableMataDate, beanName);
                            break;
                        case DEPT:
                            this.fillDept(processField, dataList, tableMataDate);
                            break;
                        case POST:
                            this.fillPost(processField, dataList, tableMataDate);
                            break;
                        case CUSTOMER:
                            this.fillCommon(processField, dataList, tableMataDate);
                            break;
                        case ROLEKEYS:
                            this.fillRoleKeys(processField, dataList, tableMataDate, beanName);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * 用户
     *
     * @param processField
     * @param datas
     * @param tableMataDate
     */
    private void fillUser(ProcessField processField, List datas, TableMataDate tableMataDate) {
        List<UserVO> userLists = userService.listUserByIds(Lists.newArrayList(processField.getIds().iterator()));
        Map<Long, Object> map = userLists.stream().collect(Collectors.toMap(UserVO::getUserId, user -> user));
        if (CollUtil.isNotEmpty(map)) {
            this.fillValue(processField, datas, map, tableMataDate);
        }
    }

    private void fillUsers(ProcessField processField, List datas, TableMataDate tableMataDate, String beanName) {
        List<BizUserBaseEntity> bizUserBaseEntities = commonMapper.listBizUser(CamelUtils.camelToUndeline(beanName), Lists.newArrayList(processField.getIds().iterator()));
        if (null != bizUserBaseEntities) {
            Set<Long> userIds = bizUserBaseEntities.stream().map(BizUserBaseEntity::getUserId).collect(Collectors.toSet());
            List<UserVO> userLists = userService.listUserByIds(Lists.newArrayList(userIds.iterator()));
            if (CollUtil.isNotEmpty(userLists)) {
                Map<Long, UserVO> userMap = userLists.stream().collect(Collectors.toMap(UserVO::getUserId, user -> user));
                for (Object data : datas) {
                    Set<UserVO> set = Sets.newHashSet();
                    for (BizUserBaseEntity bizUser : bizUserBaseEntities) {
                        Object fieldValue = tableMataDate.getFieldValue(data, processField.getFillValueFiled());
                        if (fieldValue instanceof Long) {
                            if (bizUser.getRecordId().compareTo((Long) fieldValue) == 0) {
                                UserVO userVO = userMap.get(bizUser.getUserId());
                                if (null != userVO) {
                                    UserVO userV = new UserVO();
                                    BeanUtils.copyBeanProp(userV, userVO);
                                    userV.setRecordId(((Long) fieldValue));
                                    userV.setId(bizUser.getId());
                                    set.add(userV);
                                }
                            }
                        }
                        if (!set.isEmpty()) {
                            tableMataDate.setFieldValue(data, processField.fillFiled, Lists.newArrayList(set.iterator()));
                            if (null != processField.joinField) {
                                tableMataDate.setFieldValue(data, processField.joinField, set.stream().map(UserVO::getUserId).collect(Collectors.toList()));
                            }
                        }
                    }

                }
            }
        }
    }

    /**
     * 部门
     *
     * @param processField
     * @param datas
     * @param tableMataDate
     */
    private void fillDept(ProcessField processField, List datas, TableMataDate tableMataDate) {
        List<DeptVO> deptLists = userService.listDeptByIds(Lists.newArrayList(processField.getIds().iterator()));
        if (CollUtil.isNotEmpty(deptLists)) {
            Map<Long, Object> map = deptLists.stream().collect(Collectors.toMap(DeptVO::getDeptId, DeptVO::getDeptName));
            if (CollUtil.isNotEmpty(map)) {
                this.fillValue(processField, datas, map, tableMataDate);
            }
        }
    }


    /**
     * 岗位
     *
     * @param processField
     * @param datas
     * @param tableMataDate
     */
    private void fillPost(ProcessField processField, List datas, TableMataDate tableMataDate) {
        List<PostVO> deptLists = userService.listPostByIds(Lists.newArrayList(processField.getIds().iterator()));
        if (CollUtil.isNotEmpty(deptLists)) {
            Map<Long, Object> map = deptLists.stream().collect(Collectors.toMap(PostVO::getPostId, PostVO::getPostName));
            if (CollUtil.isNotEmpty(map)) {
                this.fillValue(processField, datas, map, tableMataDate);
            }
        }
    }

    /**
     * 通用
     *
     * @param processField
     * @param datas
     * @param tableMataDate
     */
    private void fillCommon(ProcessField processField, List datas, TableMataDate tableMataDate) {
        String dbName = StringUtils.isEmpty(processField.getAlias()) ? processField.getFillFiled().getName() : processField.getAlias();
        List<CommonNameVO> list = commonMapper.listCommonName(dbName, dbName, Lists.newArrayList(processField.getIds().iterator()));
        if (CollUtil.isNotEmpty(list)) {
            Map<Long, Object> map = list.stream().collect(Collectors.toMap(CommonNameVO::getId, d -> d));
            if (CollUtil.isNotEmpty(map)) {
                this.fillValue(processField, datas, map, tableMataDate);
            }
        }
    }

    /**
     * 数据权限填充
     * 查看、操作、审核、高级权限
     *
     * @param processField
     * @param datas
     * @param tableMataDate
     * @param beanName
     */
    private void fillRoleKeys(ProcessField processField, List datas, TableMataDate tableMataDate, String beanName) {
        for (Object data : datas) {
            Set<Integer> roles = Sets.newHashSet();
            roles.add(0);

            // 归属人、参与人、负责人
            if (null != processField.roleKeysFields) {
                for (RoleKeys roleKeys : processField.getRoleKeysFields().keySet()) {
                    Object fieldValue = tableMataDate.getFieldValue(data, processField.getRoleKeysFields().get(roleKeys));
                    if (null != fieldValue) {
                        //超级管理员
                        if(SecurityUtils.isAdmin(SecurityUtils.getUserId()) || AuthUtil.hasPermi(DataRole.SHOW_ALL.getRoleKey())){
                            roles.addAll(Arrays.stream(RoleKeys.DEPT_ID.getCode()).collect(Collectors.toSet()));
                        }
                        switch (roleKeys) {
                            /** 我是归属人 **/
                            case ATTRIBUTOR_USER_ID:
                                if (fieldValue instanceof Long && ((Long) fieldValue).compareTo(SecurityUtils.getUserId()) == 0) {
                                    roles.addAll(Arrays.stream(roleKeys.getCode()).collect(Collectors.toSet()));
                                }
                                break;
                            /** 我是参与人 **/
                            case USER_IDS:
                                if (fieldValue instanceof List && ((List<Long>) fieldValue).contains(SecurityUtils.getUserId())) {
                                    roles.addAll(Arrays.stream(roleKeys.getCode()).collect(Collectors.toSet()));
                                }
                                break;
                            /** 我是负责人 **/
                            case DEPT_ID:
                                // 判断是否是负责人(有负责人，特殊权限的)
                                if (AuthUtil.hasPermi(DataRole.SHOW_ALL.getRoleKey()) || AuthUtil.hasPermi(DataRole.SHOW_SUB.getRoleKey())) {
                                    // 这条数据的归属区域是否是我管辖区域的
                                    if (fieldValue instanceof Long && SecurityUtils.getLoginUser().getSubDeptIds().contains(((Long) fieldValue))) {
                                        roles.addAll(Arrays.stream(roleKeys.getCode()).collect(Collectors.toSet()));
                                    } else {
                                        roles.addAll(Arrays.stream(RoleKeys.SHOW.getCode()).collect(Collectors.toSet()));
                                    }
                                }
                                break;
                            /** 我是创建人 **/
                            case CREATE_USER_ID:
                                if (fieldValue instanceof Long && ((Long) fieldValue).compareTo(SecurityUtils.getUserId()) == 0) {
                                    roles.addAll(Arrays.stream(roleKeys.getCode()).collect(Collectors.toSet()));
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            tableMataDate.setFieldValue(data, processField.fillFiled, Lists.newArrayList(roles.iterator()));
        }
    }

    /**
     * 填充数据
     *
     * @param processField
     * @param datas
     * @param values
     * @param tableMataDate
     */
    private void fillValue(ProcessField processField, List<?> datas, Map<Long, Object> values, TableMataDate tableMataDate) {
        for (Object data : datas) {
            Object fieldValue = tableMataDate.getFieldValue(data, processField.fillValueFiled);
            if (fieldValue instanceof Long) {
                tableMataDate.setFieldValue(data, processField.fillFiled, values.get(((Long) fieldValue)));
            }
        }
    }

    @Data
    @Accessors(chain = true)
    private class ProcessField {
        String alias;
        Field fillFiled;
        Field fillValueFiled;
        Field joinField;
        Map<RoleKeys, Field> roleKeysFields;
        Set<Long> ids;
    }
}