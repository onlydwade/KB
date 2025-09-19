package com.bytefinger.toutuo.biz.followlog.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.core.utils.CamelUtils;
import com.bytefinger.common.core.utils.DateUtils;
import com.bytefinger.common.core.utils.SpringUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.DictUtils;
import com.bytefinger.common.security.utils.SecurityUtils;
import com.bytefinger.toutuo.biz.customer.domain.CustomerFollowLogDetail;
import com.bytefinger.toutuo.biz.customer.mapper.CustomerFollowLogDetailMapper;
import com.bytefinger.toutuo.biz.oa.domain.OaMessageLog;
import com.bytefinger.toutuo.biz.oa.service.IOaMessageLogService;
import com.bytefinger.toutuo.biz.project.domain.Project;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.auditdocument.domain.AuditDocument;
import com.bytefinger.toutuo.biz.auditdocument.service.IAuditDocumentService;
import com.bytefinger.toutuo.common.domain.vo.FileVO;
import com.bytefinger.toutuo.common.enums.BizNo;
import com.bytefinger.toutuo.biz.followlog.domain.BizFollowLog;
import com.bytefinger.toutuo.biz.followlog.mapper.FollowLogMapper;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 跟踪记录 服务类
 * </p>
 *
 * @author patrick
 * @since 2022-11-17
 */
@Service
@AllArgsConstructor
public class FollowLogService {

    private final FollowLogMapper followLogMapper;

    private final IOperLogService operLogService;

    private final IAuditDocumentService projectDocumentService;

    private final IProjectService projectService;

    private final CustomerFollowLogDetailMapper customerFollowLogDetailMapper;

    private final IOaMessageLogService iOaMessageLogService;

//    private final IWorkBriefService iWorkBriefService;



    /**
     * 获取一条记录
     *
     * @param modelName
     * @param id
     * @return
     */
    @DataFill
    public BizFollowLog getById(String modelName, Long id) {
        return followLogMapper.getById(getTableName(modelName), id);
    }

    /**
     * 新增 跟踪记录
     *
     * @param bizFollowLog
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public BizFollowLog add(BizFollowLog bizFollowLog) {
        bizFollowLog.setCreateUserId(SecurityUtils.getUserId());
        bizFollowLog.setUpdateUserId(SecurityUtils.getUserId());
        bizFollowLog.setFollowTime(new Date());

        // 获取最后一次跟踪时间
        BizFollowLog lastFollowLog = followLogMapper.lastData(getTableName(bizFollowLog.getModelName()), bizFollowLog.getRecordId());
        if (null != lastFollowLog) {
            bizFollowLog.setLastFollowTime(lastFollowLog.getCreateTime());
        } else {
            Date createTime = followLogMapper.getEntityById(getTableName(bizFollowLog.getModelName()), bizFollowLog.getRecordId());
            bizFollowLog.setLastFollowTime(createTime);
        }

        // 新增记录
        followLogMapper.add(getTableName(bizFollowLog.getModelName()), bizFollowLog);


        // 修改model 跟进时间
        followLogMapper.updateEntityFollowTime(CamelUtils.camelToUndeline(bizFollowLog.getModelName()), bizFollowLog.getRecordId());

        // 记录日志
        BizNo bizNo = BizNo.findValue(bizFollowLog.getModelName());
        operLogService.changeLog(bizNo.getClazz(), bizFollowLog.getRecordId(), "新增追踪动态", null, null);


        // 特殊处理，如果是一个项目，并且追踪动态有文件，添加到项目对应节点中去
        // 特殊处理，如果是一个项目，并且追踪动态有文件，添加到项目对应节点中去
        if (bizFollowLog.getModelName().equals(Project.class.getSimpleName()) && StringUtils.isNotBlank(bizFollowLog.getFollowDocument()) && bizFollowLog.getFollowDocument().length() > 10) {
            // 新增或修改文档
            Project project = projectService.getById(bizFollowLog.getRecordId());
            AuditDocument projectDocument = new AuditDocument();
            projectDocument.setProjectId(bizFollowLog.getRecordId());
            projectDocument.setProjectType(null == project ? null : project.getProjectType());
            projectDocument.setDocmentObject(bizFollowLog.getFollowDocument());
            JSONArray files = JSON.parseArray(bizFollowLog.getFollowDocument());
            for (Object file : files) {
                FileVO fileVO = ((JSONObject) file).toJavaObject(FileVO.class);
                projectDocument.setDocumentExt(fileVO.getExt());
                projectDocument.setDocumentName(fileVO.getName());
                projectDocument.setStepMenuId(fileVO.getStepMenuId());
                projectDocument.setDocumentTemplateId(fileVO.getDocumentTemplateId());
                projectDocument.setDocmentObject(((JSONObject) file).toJSONString());
                projectDocumentService.addDocument(projectDocument);
            }
        }

        return getThis().getById(bizFollowLog.getModelName(), bizFollowLog.getId());
    }


    /**
     * 新增 跟踪记录-客户
     *
     * @param bizFollowLog
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public BizFollowLog addCustomerLog(BizFollowLog bizFollowLog) {
        bizFollowLog.setCreateUserId(SecurityUtils.getUserId());
        bizFollowLog.setUpdateUserId(SecurityUtils.getUserId());
        bizFollowLog.setFollowTime(new Date());
        Long oldFollowId= bizFollowLog.getId();
        BizFollowLog beforeFollowLog =followLogMapper.getById(bizFollowLog.getModelName(),bizFollowLog.getId());
        // 获取最后一次跟踪时间
        BizFollowLog lastFollowLog = followLogMapper.lastData(getTableName(bizFollowLog.getModelName()), bizFollowLog.getRecordId());
        if (null != lastFollowLog) {
            bizFollowLog.setLastFollowTime(lastFollowLog.getCreateTime());
        } else {
            Date createTime = followLogMapper.getEntityById(getTableName(bizFollowLog.getModelName()), bizFollowLog.getRecordId());
            bizFollowLog.setLastFollowTime(createTime);
        }

        // 新增记录
        //如果是修改，先删后加
        if (Objects.nonNull(bizFollowLog.getId()) ){
            List<Long> idsList=new ArrayList<>();
            idsList.add(bizFollowLog.getId());
            followLogMapper.deleteByIds(bizFollowLog.getModelName(), idsList);
            //followLogMapper.deleteb(Wrappers.<CustomerFollowLogDetail>lambdaQuery().eq(CustomerFollowLog::getFollowLogId,bizFollowLog.getId()));
        }
        followLogMapper.add(getTableName(bizFollowLog.getModelName()), bizFollowLog);
        //保存动态明细
        if( CollectionUtils.isNotEmpty(bizFollowLog.getCustomerFollowLogDetailList()) && bizFollowLog.getCustomerFollowLogDetailList().size() > 0  ){
            //保存动态明细
            //新增
            bizFollowLog.getCustomerFollowLogDetailList().forEach(
                    t->{
                        t.setFollowLogId(bizFollowLog.getId());
                        t.setDeleted(0);
                        t.setCreateUserId(SecurityUtils.getUserId());
                        t.setUpdateUserId(SecurityUtils.getUserId());
                        customerFollowLogDetailMapper.insert(t);
                    }
            );
        }
        // 修改model 跟进时间
        followLogMapper.updateEntityFollowTime(CamelUtils.camelToUndeline(bizFollowLog.getModelName()), bizFollowLog.getRecordId());

        // 记录日志
        BizNo bizNo = BizNo.findValue(bizFollowLog.getModelName());

        if (Objects.nonNull(oldFollowId) ){

            if(Objects.nonNull(beforeFollowLog)){
                String lonLable="编辑追踪动态:<br>";
                lonLable= lonLable+"拜访人["+beforeFollowLog.getVisitUserName()+"=>"+bizFollowLog.getVisitUserName()+"]<br>";
                String beforeTime= Objects.nonNull(beforeFollowLog.getVisitTime() ) ?DateUtils.parseDateToStr( "YYYY-MM-DD",beforeFollowLog.getVisitTime()) :"";
                String afterTime= Objects.nonNull(bizFollowLog.getVisitTime() ) ?DateUtils.parseDateToStr( "YYYY-MM-DD",bizFollowLog.getVisitTime()) :"";
                lonLable= lonLable+"拜访时间["+beforeTime+"=>"+afterTime+"]<br>";
                String beforeVisitTypeStr="空值";
                if(!StringUtils.isBlank(beforeFollowLog.getVisitType())){
                    beforeVisitTypeStr = DictUtils.getDictDataOptions("BAI_FANG_FANG_SHI", beforeFollowLog.getVisitType());
                }
                String afterVisitTypeStr="空值";
                if(!StringUtils.isBlank(bizFollowLog.getVisitType())){
                    afterVisitTypeStr = DictUtils.getDictDataOptions("BAI_FANG_FANG_SHI", bizFollowLog.getVisitType());
                }
                lonLable= lonLable+"拜访方式["+beforeVisitTypeStr+"=>"+afterVisitTypeStr+"]";
                operLogService.changeLog(bizFollowLog.getModelName(), bizFollowLog.getRecordId(), lonLable);
            }else{
                String lonLable="编辑追踪动态:<br>";
                lonLable= lonLable+"拜访人[空值=>"+bizFollowLog.getVisitUserName()+"]<br>";
                String afterTime= Objects.nonNull(bizFollowLog.getVisitTime() ) ?DateUtils.parseDateToStr( "YYYY-MM-DD",bizFollowLog.getVisitTime()) :"";
                lonLable= lonLable+"拜访时间[[空值=>"+afterTime+"]<br>";
                String afterVisitTypeStr="空值";
                if(!StringUtils.isBlank(bizFollowLog.getVisitType())){
                    afterVisitTypeStr = DictUtils.getDictDataOptions("BAI_FANG_FANG_SHI", bizFollowLog.getVisitType());
                }
                lonLable= lonLable+"拜访方式[[空值=>"+afterVisitTypeStr+"]";
                operLogService.changeLog(bizFollowLog.getModelName(), bizFollowLog.getRecordId(), lonLable);
            }
        }else{
            String lonLable="新增追踪动态:<br>";
            lonLable= lonLable+"拜访人[空值=>"+bizFollowLog.getVisitUserName()+"]<br>";
            String afterTime= Objects.nonNull(bizFollowLog.getVisitTime() ) ?DateUtils.parseDateToStr( "YYYY-MM-DD",bizFollowLog.getVisitTime()) :"";
            lonLable= lonLable+"拜访时间[[空值=>"+afterTime+"]<br>";
            String afterVisitTypeStr="空值";
            if(!StringUtils.isBlank(bizFollowLog.getVisitType())){
                afterVisitTypeStr = DictUtils.getDictDataOptions("BAI_FANG_FANG_SHI", bizFollowLog.getVisitType());
            }
            lonLable= lonLable+"拜访方式[[空值=>"+afterVisitTypeStr+"]";
            operLogService.changeLog(bizFollowLog.getModelName(), bizFollowLog.getRecordId(), lonLable);
        }



        // 特殊处理，如果是一个项目，并且追踪动态有文件，添加到项目对应节点中去
        if (bizFollowLog.getModelName().equals(Project.class.getSimpleName()) && StringUtils.isNotBlank(bizFollowLog.getFollowDocument()) && bizFollowLog.getFollowDocument().length() > 10) {
            // 新增或修改文档
            Project project = projectService.getById(bizFollowLog.getRecordId());
            AuditDocument projectDocument = new AuditDocument();
            projectDocument.setProjectId(bizFollowLog.getRecordId());
            projectDocument.setProjectType(null == project ? null : project.getProjectType());
            projectDocument.setDocmentObject(bizFollowLog.getFollowDocument());
            JSONArray files = JSON.parseArray(bizFollowLog.getFollowDocument());
            for (Object file : files) {
                FileVO fileVO = ((JSONObject) file).toJavaObject(FileVO.class);
                projectDocument.setDocumentExt(fileVO.getExt());
                projectDocument.setDocumentName(fileVO.getName());
                projectDocument.setStepMenuId(fileVO.getStepMenuId());
                projectDocument.setDocumentTemplateId(fileVO.getDocumentTemplateId());
                projectDocument.setDocmentObject(((JSONObject) file).toJSONString());
                projectDocumentService.addDocument(projectDocument);
            }
        }

        // 处理oa待办-设为已办
        LambdaQueryWrapper<OaMessageLog> oaMessageQw = new LambdaQueryWrapper<>();
        oaMessageQw.eq(OaMessageLog::getServiceId,bizFollowLog.getRecordId());
        oaMessageQw.ne(OaMessageLog::getStatus,2);
        oaMessageQw.eq(OaMessageLog::getModuleType,3);
        // 客户id、状态不等于2(已办)、数据类型为3(客户)的日志列表
        List<OaMessageLog> logList = iOaMessageLogService.list(oaMessageQw);
        if(ObjectUtil.isNotEmpty(logList)){
            for (OaMessageLog log : logList){
                // 发送oa已办
                //iWorkBriefService.setOaMessage(log);
                log.setStatus(2);
            }
            iOaMessageLogService.updateBatchById(logList);
        }
        return getThis().getById(bizFollowLog.getModelName(), bizFollowLog.getId());
    }



    /**
     * 删除 跟踪记录
     *
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteCustomerLogByIds(String modelName, String ids) {
        String[] split;
        if (StringUtils.isNotEmpty(ids) && (split = ids.split(",")).length > 0) {
            BizFollowLog entity = this.getById(modelName, Long.valueOf(split[0]));
            if (null != entity) {
                List<Long> idsList = Lists.newArrayList();
                idsList.add(entity.getId());
                followLogMapper.deleteByIds(getTableName(modelName), idsList);
                operLogService.changeLog(modelName, entity.getRecordId(), "删除追踪动态");
                //operLogService.changeLog(modelName, entity.getRecordId(), "删除追踪动态", null, null);
            }
        }
    }

    /**
     * 修改 跟踪记录
     *
     * @param bizFollowLog
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public BizFollowLog update(BizFollowLog bizFollowLog) {
        bizFollowLog.setUpdateUserId(SecurityUtils.getUserId());
        followLogMapper.update(getTableName(bizFollowLog.getModelName()), bizFollowLog);

        // 修改model跟进时间
        followLogMapper.updateEntityFollowTime(CamelUtils.camelToUndeline(bizFollowLog.getModelName()), bizFollowLog.getRecordId());

        // 记录日志
        BizNo bizNo = BizNo.findValue(bizFollowLog.getModelName());
        operLogService.changeLog(bizNo.getClazz(), bizFollowLog.getRecordId(), "修改追踪动态", null, null);

        return getThis().getById(bizFollowLog.getModelName(), bizFollowLog.getId());
    }

    /**
     * 删除 跟踪记录
     *
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteByIds(String modelName, String ids) {
        String[] split;
        if (StringUtils.isNotEmpty(ids) && (split = ids.split(",")).length > 0) {
            BizFollowLog entity = this.getById(modelName, Long.valueOf(split[0]));
            if (null != entity) {
                List<Long> idsList = Lists.newArrayList();
                idsList.add(entity.getId());
                followLogMapper.deleteByIds(getTableName(modelName), idsList);
                operLogService.changeLog(entity.getClass(), entity.getRecordId(), "删除追踪动态", null, null);
            }
        }
    }

    /**
     * 带文件的跟踪记录
     *
     * @param modelName
     * @param recordId
     * @return
     */
    public List<String> getFiles(String modelName, Long recordId) {
        return followLogMapper.getFiles(CamelUtils.camelToUndeline(modelName), recordId);
    }

    /**
     * 分页 跟踪记录
     *
     * @param queryPage
     * @return
     */
    @DataFill
    public IPage page(String modelName, QueryPage queryPage) {
        return followLogMapper.list(queryPage.toPage(), getTableName(modelName), queryPage.getWrapper());
    }



    /**
     * 分页 跟踪记录
     *
     * @param queryPage
     * @return
     */
    @DataFill
    public IPage getfollowList(String modelName, QueryPage queryPage) {
        IPage<BizFollowLog> page =followLogMapper.list(queryPage.toPage(), getTableName(modelName), queryPage.getWrapper());

         if( page.getRecords().size()>0 ){
//            List<Long> logIds=page.getRecords().stream().map(v -> v.getId()).collect(Collectors.toList());
//            list =customerFollowLogDetailMapper.selectList(Wrappers.<CustomerFollowLogDetail>lambdaQuery()
//                            .in(CustomerFollowLogDetail::getFollowLogId, logIds)
//                            .eq(CustomerFollowLogDetail::getDeleted, "0"));
            //如果有数据，根据返回数据进行遍历
            page.getRecords().forEach(t->{
                List<CustomerFollowLogDetail> list=new ArrayList<>();
                list =customerFollowLogDetailMapper.selectList(Wrappers.<CustomerFollowLogDetail>lambdaQuery()
                            .eq(CustomerFollowLogDetail::getFollowLogId, t.getId())
                            .eq(CustomerFollowLogDetail::getDeleted, "0"));
                if(CollectionUtils.isNotEmpty(list) ){
                    t.setCustomerFollowLogDetailList(list);
                }
            });
        }
        return page;
    }

    private FollowLogService getThis() {
        return SpringUtils.getBean(this.getClass());
    }

    private String getTableName(String modelName) {
        BizNo value = BizNo.findValue(modelName);
        if (null == value) {
            throw new ServiceException("数据格式错误");
        }
        return CamelUtils.camelToUndeline(value.getClazz().getSimpleName());
    }

}
