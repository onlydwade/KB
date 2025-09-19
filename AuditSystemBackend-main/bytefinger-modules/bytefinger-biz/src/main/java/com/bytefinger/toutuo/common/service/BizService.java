package com.bytefinger.toutuo.common.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.domain.BaseEntity;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.core.utils.*;
import com.bytefinger.common.core.utils.ip.IpUtils;
import com.bytefinger.common.core.web.domain.vo.CommonNameVO;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.security.annotation.Cnarea;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.annotation.Dict;
import com.bytefinger.common.security.enums.RoleKeys;
import com.bytefinger.common.security.utils.CnareaUtils;
import com.bytefinger.common.security.utils.DictUtils;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocument;
import com.bytefinger.toutuo.biz.companydocument.domain.ProjectCompanyDocumentTemplate;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentService;
import com.bytefinger.toutuo.biz.companydocument.service.IProjectCompanyDocumentTemplateService;
import com.bytefinger.toutuo.biz.companyfinance.domain.ProjectCompanyPayment;
import com.bytefinger.toutuo.biz.operlog.domain.OperLog;
import com.bytefinger.toutuo.biz.operlog.domain.ProjectExtensionOperLog;
import com.bytefinger.toutuo.biz.operlog.domain.vo.UpdateLog;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import com.bytefinger.toutuo.biz.operlog.service.IProjectExtensionOperLogService;
import com.bytefinger.toutuo.biz.projectextension.entity.BizProjectBaseEntity;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectCheck;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExpansionDocument;
import com.bytefinger.toutuo.biz.projectextension.entity.ProjectExpansionDocumentTemplate;
import com.bytefinger.toutuo.biz.projectextension.enums.ProjectModuleName;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExpansionDocumentService;
import com.bytefinger.toutuo.biz.projectextension.service.IProjectExpansionDocumentTemplateService;
import com.bytefinger.toutuo.common.annotation.HistoryFieldLog;
import com.bytefinger.toutuo.common.constants.BizConstant;
import com.bytefinger.toutuo.common.domain.BizCommonBaseEntity;
import com.bytefinger.toutuo.common.domain.BizProjectCompanyBaseEntity;
import com.bytefinger.toutuo.common.enums.BizNo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 业务通用处理类
 *
 * @author pat
 * @date 2022/10/19 13:16
 **/
@Slf4j
public class BizService<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {

    @Autowired
    protected IOperLogService operLogService;

    @Autowired
    protected IProjectExtensionOperLogService projectExtensionOperLogService;

    @Resource
    private BizNoService bizNoService;

    @Resource
    private IProjectCompanyDocumentTemplateService projectCompanyDocumentTemplateService;

    @Resource
    private IProjectExpansionDocumentTemplateService projectExpansionDocumentTemplateService;

    @Resource
    private IProjectCompanyDocumentService projectCompanyDocumentService;

    @Resource
    private IProjectExpansionDocumentService projectExpansionDocumentService;

    private BizService<M, T> me() {
        return SpringUtils.getBean(this.getClass());
    }

    /**
     * 生成编码
     *
     * @return
     */
    public String getNo() {
        return bizNoService.getNo(BizNo.findValue(super.getEntityClass().getSimpleName()));
    }

    /**
     * 通过id获取数据，过滤MP逻辑删除的数据
     *
     * @param id
     * @return
     */
    @Override
    @DataFill
    public T getById(Serializable id) {
        T entity = super.getById(id);
        if (null == entity) {
            throw new ServiceException("数据不存在，请刷新页面");
        }
        return entity;
    }

    /**
     * 1、新增、保存日志、
     * 2、插入竞对、参与人
     * 3、返回填充后的对象
     *
     * @param
     * @param entity
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public T add4Log(T entity) {
        super.save(entity);
        this.changeLog(entity.getClass(), entity.getId(), "创建数据");
        return entity;
    }

    /**
     * 1、新增、保存日志、
     * 2、插入竞对、参与人
     * 3、返回填充后的对象
     *
     * @param
     * @param entity
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public T addNoLog(T entity) {
        super.save(entity);
        return me().getById(entity.getId());
    }

    /**
     * 修改并记录日志
     *
     * @param entity
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public T update4Log(T entity) {
        // 查询修改前的数据
        T source = me().getById(entity.getId());

        if (null == source) {
            throw new ServiceException("数据不存在，请刷新页面");
        }

        // 判断当前操作人是否可修改此数据
        if (source instanceof BizCommonBaseEntity) {
            if (!validOperRole((BizCommonBaseEntity) source)) {
                throw new ServiceException("数据不存在，请刷新页面");
            }
        }

        // 正常数据修改业务
        boolean result = super.updateById(entity);

        // 修改成功，记录变更记录
        if (result) {
            T target = me().getById(entity.getId());
            try {
                comparatorObject(source, target);
            } catch (RuntimeException e) {
                log.error(e.getMessage());
            }

            return target;
        }

        return source;
    }

    /**
     * 变更归属人
     *
     * @param id
     * @param dispatchUserId
     * @return
     */


    /**
     * 删除数据
     *
     * @param entity
     * @param remarks
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(T entity, String remarks) {
        super.removeById(entity);
        operLogService.changeLog(entity.getClass(), entity.getId(), "数据被删除", null, remarks);
    }

    /**
     * 1、反射取字段
     * 2、对比值，并记录
     *
     * @param src
     * @param dist
     * @return
     */
    private void comparatorObject(BaseEntity src, BaseEntity dist) {
        if (null == src || null == dist) {
            return;
        }

        Class srcClazz = src.getClass();
        Class distClazz = dist.getClass();

        if (!srcClazz.isAssignableFrom(distClazz)) {
            return;
        }

        // 逐级父类取数据
        List<Field> entityFields = Lists.newArrayList();
        Class tempClazz = distClazz;
        while (tempClazz != null) {
            entityFields.addAll(Arrays.asList(tempClazz.getDeclaredFields()));
            tempClazz = tempClazz.getSuperclass();
        }

        // 循环字段对比
        List<UpdateLog> reList = Lists.newArrayList();
        for (Field field : entityFields) {
            // 判断字段是否有需要记录日志的标记
            HistoryFieldLog hflAnno = field.getAnnotation(HistoryFieldLog.class);
            if (null == hflAnno) {
                continue;
            }


            // 获取字段对应的值
            try {
                Object srcValue = ReflectionUtils.getFieldValue(src, field);
                Object distValue = ReflectionUtils.getFieldValue(dist, field);

                if (null == srcValue && null == distValue) {
                    continue;
                }
                if (null != srcValue && null != distValue && srcValue.equals(distValue)) {
                    continue;
                }
                String srcLog = StringUtils.nvEmpty(srcValue);
                String distLog = StringUtils.nvEmpty(distValue);

                if (srcValue instanceof String || distValue instanceof String) {
                    Dict dict = field.getAnnotation(Dict.class);
                    if (null != dict) {
                        srcLog = DictUtils.getValue4Field(field, dict, srcLog);
                        distLog = DictUtils.getValue4Field(field, dict, distLog);
                    }
                } else if (srcValue instanceof Long || distValue instanceof Long) {
                    Cnarea cnarea = field.getAnnotation(Cnarea.class);
                    if (null != cnarea) {
                        srcLog = CnareaUtils.getValue4Field(field, cnarea, srcLog);
                        distLog = CnareaUtils.getValue4Field(field, cnarea, distLog);
                    } else if (null == cnarea && ObjectUtil.isNotEmpty(hflAnno.joinField())) {
                        // 归属人解析
                        Object srcObj = ReflectionUtils.getFieldValue(src, hflAnno.joinField());
                        Object distObj = ReflectionUtils.getFieldValue(dist, hflAnno.joinField());
                        if (srcObj instanceof UserVO || distObj instanceof UserVO) {
                            srcLog = null != srcObj ? ((UserVO) srcObj).getRealname() : BizConstant.BIZ_LOG_NULL;
                            distLog = null != distObj ? ((UserVO) distObj).getRealname() : BizConstant.BIZ_LOG_NULL;
                        } else if (srcObj instanceof CommonNameVO || distObj instanceof CommonNameVO) {
                            srcLog = null != srcObj ? ((CommonNameVO) srcObj).getName() : BizConstant.BIZ_LOG_NULL;
                            distLog = null != distObj ? ((CommonNameVO) distObj).getName() : BizConstant.BIZ_LOG_NULL;
                        } else if (srcObj instanceof String || distObj instanceof String) {
                            srcLog = null != srcObj ? (String) srcObj : BizConstant.BIZ_LOG_NULL;
                            distLog = null != distObj ? (String) distObj : BizConstant.BIZ_LOG_NULL;
                        }
                    }
                } else if (srcValue instanceof Date || distValue instanceof Date) {
                    srcLog = null == srcValue ? "" : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, (Date) srcValue);
                    distLog = null == distValue ? "" : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, (Date) distValue);
                } else if (ObjectUtil.isBasicType(srcValue) || ObjectUtil.isBasicType(distValue)) {
                    // TODO 其他基本类型字段
                }

                srcLog = StringUtils.isEmpty(srcLog) ? BizConstant.BIZ_LOG_NULL : srcLog;
                distLog = StringUtils.isEmpty(distLog) ? BizConstant.BIZ_LOG_NULL : distLog;

                if (srcLog.equals(distLog)) {
                    continue;
                }

                // 创建日志
                reList.add(UpdateLog.builder()
                        .fieldName(hflAnno.value())
                        .valueBefore(srcLog)
                        .valueAfter(distLog)
                        .build());

            } catch (RuntimeException e) {
                log.error(e.getMessage());
                continue;
            }
        }

        if (!reList.isEmpty()) {
            operLogService.save(new OperLog()
                    .setRecordId(src.getId())
                    .setUpdateDeptId(SecurityUtils.getLoginUser().getDeptId())
                    .setUpdatePostId(SecurityUtils.getLoginUser().getPostId())
                    .setModuleName(srcClazz.getSimpleName())
                    .setUpdateLog(JSON.toJSONString(reList))
                    .setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest())));
        }
    }

    /**
     * 验证数据权限
     *
     * @param entity
     * @return
     */
    protected boolean validOperRole(BizCommonBaseEntity entity) {
        return RoleKeys.dataShow(entity.getRoleKeys());
    }

    /**
     * TODO 判断数据是否可变更状态，状态机中查询是否可跳转到此后的状态中
     *
     * @param vStatus 验证状态
     * @param bStatus 之前的状态
     * @return
     */
    protected boolean statusValid(Integer vStatus, Integer bStatus, Integer... aStatus) {
        // 当前状态和验证状态不相等
        if (null != bStatus && !vStatus.equals(bStatus)) {
            return false;
        }
        return true;
    }

    protected void changeLog(Class clazz, Long id, String title) {
        operLogService.changeLog(clazz, id, title, null, null);
    }


    protected void dataFillDcoument(Long stepMenuId, IPage<? extends BizProjectCompanyBaseEntity> page) {
        List<ProjectCompanyDocumentTemplate> list = projectCompanyDocumentTemplateService.list(Wrappers.<ProjectCompanyDocumentTemplate>lambdaQuery()
                .eq(ProjectCompanyDocumentTemplate::getStepMenuId, stepMenuId));

        try {
            for (BizProjectCompanyBaseEntity record : page.getRecords()) {
                List<ProjectCompanyDocumentTemplate> templateList = new ArrayList<>(list);
                for (ProjectCompanyDocumentTemplate template : templateList) {
                    List<ProjectCompanyDocument> documents = projectCompanyDocumentService.list(Wrappers.<ProjectCompanyDocument>lambdaQuery()
                            .eq(ProjectCompanyDocument::getDocumentTemplateId, template.getId())
                            .eq(ProjectCompanyDocument::getCompanyId, record.getCompanyId())
                            .eq(ProjectCompanyDocument::getRecordId, record.getId()));
                    // 序列化原列表
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(documents);
                    oos.close();
                    // 反序列化得到新列表
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    template.setProjectCompanyDocumentList((List<ProjectCompanyDocument>) ois.readObject());
                }

                // 序列化原列表
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(templateList);
                oos.close();
                // 反序列化得到新列表
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                record.setDocumentTemplateList((List<ProjectCompanyDocumentTemplate>) ois.readObject());
            }

        } catch (Exception e) {
            throw new RuntimeException("异常");
        }
    }


    protected void dataProjectFillDcoument(Long stepMenuId, List<BizProjectBaseEntity> projectBaseEntities,Integer notTemplate) {
        List<ProjectExpansionDocumentTemplate> list = projectExpansionDocumentTemplateService.list(Wrappers.<ProjectExpansionDocumentTemplate>lambdaQuery()
                .eq(ProjectExpansionDocumentTemplate::getStepMenuId, stepMenuId));

        try {
            for (BizProjectBaseEntity projectBaseEntitie : projectBaseEntities) {
                List<ProjectExpansionDocumentTemplate> templateList = new ArrayList<>(list);
                for (ProjectExpansionDocumentTemplate template : templateList) {

                    LambdaQueryWrapper<ProjectExpansionDocument> wrapper = new LambdaQueryWrapper<ProjectExpansionDocument>();
                    if (notTemplate == null){
                        wrapper.eq(ProjectExpansionDocument::getDocumentTemplateId, template.getId());
                    }
                    wrapper.eq(ProjectExpansionDocument::getStepMenuId, stepMenuId);
                    wrapper.eq(ProjectExpansionDocument::getProjectId, projectBaseEntitie.getProjectId());
                    wrapper.eq(ProjectExpansionDocument::getRecordId, projectBaseEntitie.getId());
                    List<ProjectExpansionDocument> documents = projectExpansionDocumentService.list(wrapper);
                    // 序列化原列表
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(documents);
                    oos.close();
                    // 反序列化得到新列表
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    template.setProjectCompanyDocumentList((List<ProjectExpansionDocument>) ois.readObject());
                }

                // 序列化原列表
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(templateList);
                oos.close();
                // 反序列化得到新列表
                ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                ObjectInputStream ois = new ObjectInputStream(bais);
                projectBaseEntitie.setDocumentTemplateList((List<ProjectExpansionDocumentTemplate>) ois.readObject());
            }

        } catch (Exception e) {
            throw new RuntimeException("异常");
        }
    }



    /**
     * 1、新增、保存日志、
     * 2、插入竞对、参与人
     * 3、返回填充后的对象
     *
     * @param
     * @param entity
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public T addExtension4Log(T entity,Long projectId) {
        super.save(entity);
        if (null == projectId){
            throw new ServiceException("项目不存在");
        }
        Class c = entity.getClass();
        ProjectModuleName projectModuleName = ProjectModuleName.getByCode(c.getSimpleName());
        this.changeExtensionLog(c, entity.getId(), projectModuleName != null ? projectModuleName.getDesc() + "模块创建数据" : "创建数据",projectId);
        return entity;
    }

    /**
     * 修改并记录日志
     *
     * @param entity
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public T updateExtension4Log(T entity,Long projectId) {
        // 查询修改前的数据
        T source = me().getById(entity.getId());
        if (null == projectId){
            throw new ServiceException("项目不存在");
        }
        if (null == source) {
            throw new ServiceException("数据不存在，请刷新页面");
        }

        // 判断当前操作人是否可修改此数据
        if (source instanceof BizCommonBaseEntity) {
            if (!validOperRole((BizCommonBaseEntity) source)) {
                throw new ServiceException("数据不存在，请刷新页面");
            }
        }

        // 正常数据修改业务
        boolean result = super.updateById(entity);

        // 修改成功，记录变更记录
        if (result) {
            T target = me().getById(entity.getId());
            try {
                comparatorExtensionObject(source, target,projectId);
            } catch (RuntimeException e) {
                log.error(e.getMessage());
            }

            return target;
        }

        return source;
    }

    /**
     * 1、反射取字段
     * 2、对比值，并记录
     *
     * @param src
     * @param dist
     * @return
     */
    private void comparatorExtensionObject(BaseEntity src, BaseEntity dist,Long projectId) {
        if (null == src || null == dist) {
            return;
        }

        Class srcClazz = src.getClass();
        Class distClazz = dist.getClass();

        if (!srcClazz.isAssignableFrom(distClazz)) {
            return;
        }

        // 逐级父类取数据
        List<Field> entityFields = Lists.newArrayList();
        Class tempClazz = distClazz;
        while (tempClazz != null) {
            entityFields.addAll(Arrays.asList(tempClazz.getDeclaredFields()));
            tempClazz = tempClazz.getSuperclass();
        }

        // 循环字段对比
        List<UpdateLog> reList = Lists.newArrayList();
        for (Field field : entityFields) {
            // 判断字段是否有需要记录日志的标记
            HistoryFieldLog hflAnno = field.getAnnotation(HistoryFieldLog.class);
            if (null == hflAnno) {
                continue;
            }


            // 获取字段对应的值
            try {
                Object srcValue = ReflectionUtils.getFieldValue(src, field);
                Object distValue = ReflectionUtils.getFieldValue(dist, field);

                if (null == srcValue && null == distValue) {
                    continue;
                }
                if (null != srcValue && null != distValue && srcValue.equals(distValue)) {
                    continue;
                }
                String srcLog = StringUtils.nvEmpty(srcValue);
                String distLog = StringUtils.nvEmpty(distValue);

                if (srcValue instanceof String || distValue instanceof String) {
                    Dict dict = field.getAnnotation(Dict.class);
                    if (null != dict) {
                        srcLog = DictUtils.getValue4Field(field, dict, srcLog);
                        distLog = DictUtils.getValue4Field(field, dict, distLog);
                    }
                } else if (srcValue instanceof Long || distValue instanceof Long) {
                    Cnarea cnarea = field.getAnnotation(Cnarea.class);
                    if (null != cnarea) {
                        srcLog = CnareaUtils.getValue4Field(field, cnarea, srcLog);
                        distLog = CnareaUtils.getValue4Field(field, cnarea, distLog);
                    } else if (null == cnarea && ObjectUtil.isNotEmpty(hflAnno.joinField())) {
                        // 归属人解析
                        Object srcObj = ReflectionUtils.getFieldValue(src, hflAnno.joinField());
                        Object distObj = ReflectionUtils.getFieldValue(dist, hflAnno.joinField());
                        if (srcObj instanceof UserVO || distObj instanceof UserVO) {
                            srcLog = null != srcObj ? ((UserVO) srcObj).getRealname() : BizConstant.BIZ_LOG_NULL;
                            distLog = null != distObj ? ((UserVO) distObj).getRealname() : BizConstant.BIZ_LOG_NULL;
                        } else if (srcObj instanceof CommonNameVO || distObj instanceof CommonNameVO) {
                            srcLog = null != srcObj ? ((CommonNameVO) srcObj).getName() : BizConstant.BIZ_LOG_NULL;
                            distLog = null != distObj ? ((CommonNameVO) distObj).getName() : BizConstant.BIZ_LOG_NULL;
                        } else if (srcObj instanceof String || distObj instanceof String) {
                            srcLog = null != srcObj ? (String) srcObj : BizConstant.BIZ_LOG_NULL;
                            distLog = null != distObj ? (String) distObj : BizConstant.BIZ_LOG_NULL;
                        }
                    }
                } else if (srcValue instanceof Date || distValue instanceof Date) {
                    srcLog = null == srcValue ? "" : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, (Date) srcValue);
                    distLog = null == distValue ? "" : DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, (Date) distValue);
                } else if (ObjectUtil.isBasicType(srcValue) || ObjectUtil.isBasicType(distValue)) {
                    // TODO 其他基本类型字段
                }

                srcLog = StringUtils.isEmpty(srcLog) ? BizConstant.BIZ_LOG_NULL : srcLog;
                distLog = StringUtils.isEmpty(distLog) ? BizConstant.BIZ_LOG_NULL : distLog;

                if (srcLog.equals(distLog)) {
                    continue;
                }

                // 创建日志
                reList.add(UpdateLog.builder()
                        .fieldName(hflAnno.value())
                        .valueBefore(srcLog)
                        .valueAfter(distLog)
                        .build());

            } catch (RuntimeException e) {
                log.error(e.getMessage());
                continue;
            }
        }

        if (!reList.isEmpty()) {
            projectExtensionOperLogService.save(new ProjectExtensionOperLog()
                    .setRecordId(src.getId())
                    .setUpdateDeptId(SecurityUtils.getLoginUser().getDeptId())
                    .setUpdatePostId(SecurityUtils.getLoginUser().getPostId())
                    .setModuleName(srcClazz.getSimpleName())
                    .setUpdateLog(JSON.toJSONString(reList))
                    .setProjectId(projectId)
                    .setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest())));
        }
    }

    protected void changeExtensionLog(Class clazz, Long id, String title,Long projectId) {
        projectExtensionOperLogService.changeLog(clazz, id, title, null, null,projectId);
    }
    /**
     * 删除数据
     *
     * @param entity
     * @param remarks
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteExtension(T entity, String remarks,Long projectId) {
        if (null == projectId){
            throw new ServiceException("项目不存在");
        }
        super.removeById(entity);
        Class c = entity.getClass();
        ProjectModuleName projectModuleName = ProjectModuleName.getByCode(c.getSimpleName());
        projectExtensionOperLogService.changeLog(c, entity.getId(), projectModuleName != null ? projectModuleName.getDesc() + "模块删除数据" : "删除数据", null, remarks,projectId);
    }
}

