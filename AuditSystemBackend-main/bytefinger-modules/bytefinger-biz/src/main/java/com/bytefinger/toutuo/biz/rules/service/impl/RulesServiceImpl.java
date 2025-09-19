package com.bytefinger.toutuo.biz.rules.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bytefinger.common.core.exception.ServiceException;
import com.bytefinger.common.core.utils.*;
import com.bytefinger.common.core.web.domain.vo.TreeEntity;
import com.bytefinger.common.core.web.domain.vo.UserVO;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.redis.lock.annotation.DLock;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.DictUtils;
import com.bytefinger.toutuo.api.biz.user.domain.SysUser;
import com.bytefinger.toutuo.api.system.core.domain.SysDictData;
import com.bytefinger.toutuo.api.system.core.domain.SysMessage;
import com.bytefinger.toutuo.biz.oa.config.OaProperties;
import com.bytefinger.toutuo.biz.oa.domain.OaMessageLog;
import com.bytefinger.toutuo.biz.oa.service.IOaMessageLogService;
import com.bytefinger.toutuo.biz.operlog.domain.vo.UpdateLog;
import com.bytefinger.toutuo.biz.operlog.service.IOperLogService;
import com.bytefinger.toutuo.biz.project.service.IProjectService;
import com.bytefinger.toutuo.biz.projectstep.dto.OaTodo;
import com.bytefinger.toutuo.biz.rules.domain.Rules;
import com.bytefinger.toutuo.biz.rules.domain.RulesDict;
import com.bytefinger.toutuo.biz.rules.domain.RulesMessageLog;
import com.bytefinger.toutuo.biz.rules.domain.dto.RuleActionDTO;
import com.bytefinger.toutuo.biz.rules.domain.dto.RuleConditionDTO;
import com.bytefinger.toutuo.biz.rules.enums.RulesIsAssociationTableEnum;
import com.bytefinger.toutuo.biz.rules.enums.RulesSendObject;
import com.bytefinger.toutuo.biz.rules.enums.RulesVal;
import com.bytefinger.toutuo.biz.rules.mapper.RulesDictMapper;
import com.bytefinger.toutuo.biz.rules.mapper.RulesMapper;
import com.bytefinger.toutuo.biz.rules.mapper.RulesMessageLogMapper;
import com.bytefinger.toutuo.biz.rules.service.IRulesService;
import com.bytefinger.toutuo.biz.user.mapper.SysUserMapper;
import com.bytefinger.toutuo.biz.user.service.UserService;
import com.bytefinger.toutuo.common.constants.BizConstant;
import com.bytefinger.toutuo.common.domain.BizUserBaseEntity;
import com.bytefinger.toutuo.common.mapper.CommonMapper;
import com.bytefinger.toutuo.common.service.SendMessageService;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.jsonwebtoken.lang.Collections;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 规则管理 服务实现类
 * </p>
 *
 * @author patrick
 * @since 2022-11-01
 */

//@AllArgsConstructor 此注解会生成一个包含所有变量全参的构造方法。不能使用，因为下面使用了@Value,会导致系统无法启动报错，因为：
// @Value("${属性名}") 注解是通过对象的set 方法赋值的，构造方法的执行在set方法之前，所以在构造方法中使用变量会发现变量为 null
//解决办法：将AllArgsConstructor改成RequiredArgsConstructor，然后把需要注入的bean改成final类型的，就可以了
//
@Service
@Slf4j
@RequiredArgsConstructor
public class RulesServiceImpl extends ServiceImpl<RulesMapper, Rules> implements IRulesService {

    private final RulesDictMapper rulesDictMapper;

    private final CommonMapper commonMapper;

    private final IOperLogService operLogService;

    private final SendMessageService sendMessageService;

    private final UserService userService;
    private final SysUserMapper userMapper;
    private final OaProperties oaProperties;
    private final RulesMessageLogMapper rulesMessageLogMapper;
    private final IOaMessageLogService iOaMessageLogService;
    private final IProjectService iProjectService;

    //根据岗位编码查询用户ID
    @Value("${user.deptName}")
    public String deptName;
    @Value("${rule.expireOnOff}")
    public boolean expireOnOff;

    @Override
    public Rules add(Rules rules) {
        if (super.count(Wrappers.<Rules>lambdaQuery().eq(Rules::getRuleLevel, rules.getRuleLevel())) > 0) {
            throw new ServiceException("级别已存在，请更换");
        }

        if (super.count(Wrappers.<Rules>lambdaQuery().eq(Rules::getRuleName, rules.getRuleName())) > 0) {
            throw new ServiceException("规则名称重复，请更换");
        }

        rules.setCondition(JSON.toJSONString(rules.getConditionList()));
        rules.setAction(JSON.toJSONString(rules.getActionList()));
        super.save(rules);
        return getThis().getById(rules.getId());
    }

    @Override
    public Rules update(Rules rules) {
        if (super.count(Wrappers.<Rules>lambdaQuery()
                .eq(Rules::getRuleLevel, rules.getRuleLevel())
                .ne(Rules::getId, rules.getId())) > 0) {
            throw new ServiceException("级别已存在，请更换");
        }

        rules.setCondition(JSON.toJSONString(rules.getConditionList()));
        rules.setAction(JSON.toJSONString(rules.getActionList()));
        super.updateById(rules);
        return getThis().getById(rules.getId());
    }

    @DataFill
    @Override
    public Rules getById(Serializable id) {
        return super.getById(id);
    }

    @DataFill
    @Override
    public IPage page(QueryPage queryPage) {
        return super.page(queryPage.toPage(), queryPage.getWrapper());
    }

    /**
     * 类型status：（9-树形值、8-业务字段、7-时间字段、6-动作：变更字段 5-动作：发送消息通知）
     *
     * @return
     */
    @Override
    public Map<String, Object> rulesEnums() {
        Map<String, Object> reMap = Maps.newLinkedHashMap();
        List<TreeEntity> reConditionList = Lists.newArrayList();
        List<TreeEntity> reActionList = Lists.newArrayList();

        List<RulesDict> rulesDicts = rulesDictMapper.selectList(Wrappers.emptyWrapper());
        Map<String, List<RulesDict>> rulesDictMap = rulesDicts.stream().collect(Collectors.groupingBy(RulesDict::getRuleGroup));
        List<RulesDict> rulesModeNames = rulesDictMap.get(RulesVal.RULES_MODE_NAME.getCode());
        List<RulesDict> rulesFieldTypes = rulesDictMap.get(RulesVal.RULES_FIELD_TYPE.getCode());
        List<RulesDict> rulesActionTypes = rulesDictMap.get(RulesVal.RULES_ACTION_TYPE.getCode());


        Rules rules = super.getOne(Wrappers.<Rules>lambdaQuery().orderByDesc(Rules::getRuleLevel).last(" limit 1 "));
        Integer rulesLevel = 1;
        if (null != rules && null != rules.getRuleLevel()) {
            rulesLevel = rules.getRuleLevel() + 1;
        }

        reMap.put("rulesLevel", rulesLevel);
        reMap.put("condition", reConditionList);
        reMap.put("action", reActionList);

        // 条件
        for (RulesDict rulesDict : rulesModeNames) {
            TreeEntity rEntity = new TreeEntity();
            rEntity.setCode(rulesDict.getRuleKey());
            rEntity.setName(rulesDict.getRuleDesc());

            //如果是投拓运营模块，只需要投拓字段数据,只需要包含投拓的业务，时间两种，以及他的分组下的所有其他包含字段列表
            if (rulesDict.getRuleKey().equals(RulesVal.TOUTUO_OPERATE.getCode())) {
                rulesFieldTypes = rulesDictMap.get(RulesVal.RULES_FIELD_TYPE.getCode()).stream().filter(
                        rulesDict1 -> rulesDict1.getRuleKey().equals(RulesVal.SHI_JIAN_TOUTUO.getCode())
                                || rulesDict1.getRuleKey().equals(RulesVal.YE_WU_TOUTUO.getCode())
                                || rulesDict1.getRuleGroup().contains(RulesVal.SHI_JIAN_TOUTUO.getCode())
                                || rulesDict1.getRuleGroup().contains(RulesVal.YE_WU_TOUTUO.getCode())
                ).collect(Collectors.toList());
            } else if (rulesDict.getRuleKey().equals(RulesVal.TOUTUO_CUSTOMER.getCode())) {
                //客户
                rulesFieldTypes = rulesDictMap.get(RulesVal.RULES_FIELD_TYPE.getCode()).stream().filter(
                        rulesDict2 -> rulesDict2.getRuleKey().equals(RulesVal.SHI_JIAN_CUSTOMER.getCode())
                                || rulesDict2.getRuleKey().equals(RulesVal.YE_WU_CUSTOMER.getCode())
                                || rulesDict2.getRuleGroup().contains(RulesVal.SHI_JIAN_CUSTOMER.getCode())
                                || rulesDict2.getRuleGroup().contains(RulesVal.YE_WU_CUSTOMER.getCode())
                ).collect(Collectors.toList());
            } else {  //如果不是投拓运营模块，需要去除投拓的字段数据，以及他组内的其他字段
                rulesFieldTypes = rulesDictMap.get(RulesVal.RULES_FIELD_TYPE.getCode()).stream().filter(
                        rulesDict2 -> rulesDict2.getRuleKey().equals(RulesVal.SHI_JIAN.getCode())
                                || rulesDict2.getRuleKey().equals(RulesVal.YE_WU.getCode())
                                || rulesDict2.getRuleKey().equals(RulesVal.XI_TONG.getCode())
                                || rulesDict2.getRuleGroup().contains(RulesVal.SHI_JIAN.getCode())
                                || rulesDict2.getRuleGroup().contains(RulesVal.YE_WU.getCode())
                                || rulesDict2.getRuleGroup().contains(RulesVal.XI_TONG.getCode())
                ).collect(Collectors.toList());
            }

            //规则条件类型 ，业务，时间
            for (RulesDict rulesFieldType : rulesFieldTypes) {
                String conditionKey = rulesDict.getRuleKey() + "_" + rulesFieldType.getRuleKey();

                TreeEntity fEntity = new TreeEntity();
                fEntity.setCode(conditionKey);
                fEntity.setName(rulesFieldType.getRuleDesc());
                fEntity.setStatus(rulesFieldType.getRuleKey().equals(RulesVal.YE_WU.getCode()) || rulesFieldType.getRuleKey().equals(RulesVal.YE_WU_TOUTUO.getCode()) || rulesFieldType.getRuleKey().equals(RulesVal.YE_WU_CUSTOMER.getCode()) || rulesFieldType.getRuleKey().equals(RulesVal.XI_TONG.getCode()) ? 8 : 7);
                rEntity.getChildren().add(fEntity);

                //时间或业务字段内的枚举，例如 项目投标保证金是否追回
                List<RulesDict> conditionList = rulesDictMap.get(conditionKey);
                if (ObjectUtil.isEmpty(conditionList)) {
                    continue;
                }
                for (RulesDict rDict : conditionList) {
                    TreeEntity rDictEntity = new TreeEntity();
                    rDictEntity.setCode(rDict.getRuleKey());
                    rDictEntity.setName(rDict.getRuleDesc());
                    fEntity.getChildren().add(rDictEntity);

                    // 业务字段
                    if (rulesFieldType.getRuleKey().equals(RulesVal.YE_WU.getCode()) || rulesFieldType.getRuleKey().equals(RulesVal.YE_WU_TOUTUO.getCode()) || rulesFieldType.getRuleKey().equals(RulesVal.YE_WU_CUSTOMER.getCode()) || rulesFieldType.getRuleKey().equals(RulesVal.XI_TONG.getCode())) {
                        if (ObjectUtil.equals(RulesVal.DEPT_ID.getCode(), rDict.getRuleValue())) {
                            rDictEntity.setStatus(9);
                        } else {
                            // 取下方值 例如 项目投标保证金是否追回内的具体参数值  是 或 否
                            List<SysDictData> valueList = DictUtils.getDictCache(rDict.getRuleValue());
                            if (null != valueList && !valueList.isEmpty()) {
                                for (SysDictData val : valueList) {
                                    TreeEntity valTreeEntity = new TreeEntity();
                                    valTreeEntity.setCode(val.getDictValue());
                                    valTreeEntity.setName(val.getDictLabel());
                                    rDictEntity.getChildren().add(valTreeEntity);
                                }
                            }
                        }
                    }
                }
            }

            reConditionList.add(rEntity);
        }

        // 动作
        for (RulesDict rulesActionType : rulesActionTypes) {
            // 消息通知
            if (rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI.getCode()) || rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode()) || rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_CUSTOMER.getCode()) || rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OA_TODO_REMIND.getCode())) {
                TreeEntity rDictEntity = new TreeEntity();
                rDictEntity.setCode(rulesActionType.getRuleKey());
                rDictEntity.setName(rulesActionType.getRuleDesc());
                rDictEntity.setStatus(5);
                //rDictEntity.setGroupName(rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode()) ? RulesVal.TOUTUO_OPERATE.getCode() : RulesVal.XIANG_MU.getCode());

                if (rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode())) {
                    rDictEntity.setGroupName(RulesVal.TOUTUO_OPERATE.getCode());
                }
                if (rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_CUSTOMER.getCode())) {
                    rDictEntity.setGroupName(RulesVal.TOUTUO_CUSTOMER.getCode());
                }
                if (rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI.getCode())) {
                    rDictEntity.setGroupName(RulesVal.XIANG_MU.getCode());
                }
                if (rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OA_TODO_REMIND.getCode())) {
                    rDictEntity.setGroupName(RulesVal.OA_TODO_REMIND.getCode());
                }
                // 取值
                List<RulesDict> valueList = rulesDictMap.get(rulesActionType.getRuleKey());
                if (null != valueList && !valueList.isEmpty()) {
                    for (RulesDict val : valueList) {
                        TreeEntity valTreeEntity = new TreeEntity();
                        valTreeEntity.setCode(val.getRuleKey());
                        valTreeEntity.setName(val.getRuleDesc());
                        //valTreeEntity.setGroupName(rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode()) ? RulesVal.TOUTUO_OPERATE.getCode() : RulesVal.XIANG_MU.getCode());
                        if (rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode())) {
                            valTreeEntity.setGroupName(RulesVal.TOUTUO_OPERATE.getCode());
                        }
                        if (rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_CUSTOMER.getCode())) {
                            valTreeEntity.setGroupName(RulesVal.TOUTUO_CUSTOMER.getCode());
                        }
                        if (rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI.getCode())) {
                            valTreeEntity.setGroupName(RulesVal.XIANG_MU.getCode());
                        }
                        if (rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OA_TODO_REMIND.getCode())) {
                            valTreeEntity.setGroupName(RulesVal.OA_TODO_REMIND.getCode());
                        }
                        rDictEntity.getChildren().add(valTreeEntity);
                    }
                }

                reActionList.add(rDictEntity);
            }

            // 变更字段
            else {
                TreeEntity ywEntity = new TreeEntity();
                ywEntity.setCode(rulesActionType.getRuleKey());
                ywEntity.setName(rulesActionType.getRuleDesc());
                ywEntity.setStatus(6);
                ywEntity.setGroupName(rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode()) ? RulesVal.TOUTUO_OPERATE.getCode() : RulesVal.XIANG_MU.getCode());


                for (RulesDict rulesDict : rulesModeNames) {
                    String conditionKey = rulesDict.getRuleKey() + "_" + rulesActionType.getRuleKey();
                    TreeEntity rEntity = new TreeEntity();
                    rEntity.setCode(rulesDict.getRuleKey());
                    rEntity.setName(rulesDict.getRuleDesc());

                    List<RulesDict> actionList = rulesDictMap.get(conditionKey);
                    if (CollectionUtil.isEmpty(actionList)) continue;
                    for (RulesDict rDict : actionList) {
                        TreeEntity rDictEntity = new TreeEntity();
                        rDictEntity.setCode(rDict.getRuleKey());
                        rDictEntity.setName(rDict.getRuleDesc());
                        rDictEntity.setGroupName(rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode()) ? RulesVal.TOUTUO_OPERATE.getCode() : RulesVal.XIANG_MU.getCode());


                        if (ObjectUtil.equals(RulesVal.DEPT_ID.getCode(), rDict.getRuleValue())) {
                            rDictEntity.setStatus(9);
                        } else {
                            // 取下方值
                            List<SysDictData> valueList = DictUtils.getDictCache(rDict.getRuleValue());
                            if (null != valueList && !valueList.isEmpty()) {
                                for (SysDictData val : valueList) {
                                    TreeEntity valTreeEntity = new TreeEntity();
                                    valTreeEntity.setCode(val.getDictValue());
                                    valTreeEntity.setName(val.getDictLabel());
                                    valTreeEntity.setGroupName(rulesActionType.getRuleKey().equals(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode()) ? RulesVal.TOUTUO_OPERATE.getCode() : RulesVal.XIANG_MU.getCode());
                                    rDictEntity.getChildren().add(valTreeEntity);
                                }
                            }
                        }
                        rEntity.getChildren().add(rDictEntity);
                    }
                    ywEntity.getChildren().add(rEntity);
                }
                reActionList.add(ywEntity);
            }
        }
        return reMap;
    }

    /**
     * 定时启动
     * 翻译规则 ==> 保存规则,直接执行SQL
     *
     * @param
     */
    @Override
    @Async
    @DLock(name = BizConstant.BIZ_TASK_LOCK_RULES_TASK, expire = 600000, waitTime = 200)
    public void ruleTask() {
        try {
            List<RulesDict> rulesDicts = rulesDictMapper.selectList(Wrappers.emptyWrapper());
            Map<String, RulesDict> rulesDictMap = rulesDicts.stream().collect(Collectors.toMap(RulesDict::getRuleKey, o -> o));

            List<Rules> list = super.list(Wrappers.<Rules>lambdaQuery().eq(Rules::getRuleStatus, 1).orderByAsc(Rules::getRuleLevel));
            for (Rules rules : list) {
                // 获取对那个对象进行处理，项目、客户
                RulesDict rulesModeName = rulesDictMap.get(rules.getModeName());

                /** 处理数据 **/
                if (StringUtils.isNotEmpty(rules.getCondition()) && StringUtils.isNotEmpty(rules.getAction())) {
                    Map<String, Object> data = this.processCondition(rules, rulesModeName, rulesDictMap);
                    List<Map> baseEntities = Convert.toList(Map.class, data.get("baseEntities"));
                    String associationTable = Convert.toStr(data.get("associationTable"));
                    // 执行SQL，获取数据
                    if (!baseEntities.isEmpty()) {
                        // 解析动作，执行动作
                        this.runAction(rules, rulesModeName, baseEntities, rulesDictMap, associationTable);
                    }
                }
            }
            // 开关是否开启
            if (expireOnOff) {
                // 处理40天未维护，并且项目评审节点未完成或者没有到项目评审这个节点的项目，标记失效
                iProjectService.handleExpireProject();
            }
        } catch (RuntimeException e) {
            String message = ExceptionUtil.stacktraceToString(e);
            log.error("规则处理异常报错,原因：{}", message);
        } finally {
            log.debug("规则任务执行完毕");
        }
    }

    /**
     * 处理规则消息，查询并返回数据
     *
     * @param rules
     * @param rulesModeName
     * @param rulesDictMap
     * @return
     */
    private Map<String, Object> processCondition(Rules rules, RulesDict rulesModeName, Map<String, RulesDict> rulesDictMap) {
        log.info("定时任务执行规则开始，规则名称={} ", rules.getRuleName());
        Map<String, Object> data = new HashMap<>();
        // 解析条件
        List<RuleConditionDTO> conditions = JSON.parseArray(rules.getCondition(), RuleConditionDTO.class);
        // 关联表表名，存在关联则同一个项目发送多条消息
        String associationTable = RulesIsAssociationTableEnum.NONE.getCode();
        if (!Collections.isEmpty(conditions)) {
            // 拼接规则，获取数据
            StringBuffer sql = new StringBuffer("SELECT * FROM " + rulesModeName.getRuleField() + " bpv WHERE 1 = 1 ");
            //如果是投拓的模块
            if (RulesVal.TOUTUO_OPERATE.getCode().equals(rules.getModeName())) {
                //1.干预下达期限，特殊处理
                long transmitDeadlineCount = conditions.stream().filter(e -> e.getFieldName().equals(RulesVal.TOUTUO_OPERATE_GAN_YU_XIA_DA_SHI_JIAN.getCode())
                ).count();
                if (transmitDeadlineCount > 0) {
                    associationTable = RulesIsAssociationTableEnum.BIZ_PROJECT_ASSESS.getCode();
                    sql = new StringBuffer("SELECT bpv.*,bpa.transmit_deadline,bpa.id as associationBusinessId FROM " + rulesModeName.getRuleField() + " bpv left join biz_project_assess bpa on bpv.id = bpa.project_id WHERE 1 = 1 AND bpa.transmit_already_state=0 ");
                }
                //2.干预实施期限和干预执行状态字段，特殊处理
                long ganyuCount = conditions.stream().filter(e -> e.getFieldName().equals(RulesVal.TOUTUO_OPERATE_GAN_YU_SHI_SHI_QI_XIAN.getCode())
                        || e.getFieldName().equals(RulesVal.TOUTUO_OPERATE_GAN_YU_ZHI_XING_ZHUANG_TAI.getCode())
                ).count();
                if (ganyuCount > 0) {
                    associationTable = RulesIsAssociationTableEnum.BIZ_PROJECT_INTERVENE.getCode();
                    sql = new StringBuffer("SELECT bpv.*,bpi.create_time as intervene_create_time,bpi.intervene_deadline,bpi.intervene_state,bpi.scheme_user_id,bpi.id as associationBusinessId FROM " + rulesModeName.getRuleField() + " bpv left join biz_project_intervene bpi on bpv.id = bpi.project_id  WHERE 1 = 1 ");
                }
                //3.如果同时存在干预实施期限和干预执行状态字段，干预下达期限 两种的情况
                if (transmitDeadlineCount > 0 && ganyuCount > 0) {
                    associationTable = RulesIsAssociationTableEnum.BIZ_PROJECT_ASSESS_AND_BIZ_PROJECT_INTERVENE.getCode();
                    sql = new StringBuffer("SELECT bpv.*,bpa.transmit_deadline,bpi.id as associationBusinessId FROM " + rulesModeName.getRuleField() + " bpv left join biz_project_assess bpa on bpv.id = bpa.project_id left join biz_project_intervene bpi on bpa.id = bpi.assess_id  WHERE 1 = 1 AND bpa.transmit_already_state=0 ");
                }
                //判断是不是合同服务时间的时间规则，这个字段比较特殊，需要增加一个查询条件
                long contractServiceStartTimeCount = conditions.stream().filter(e -> e.getFieldName().equals(RulesVal.TOUTUO_OPERATE_HE_TONG_FU_WU_KAI_SHI_SHI_JIAN.getCode())).count();
                if (contractServiceStartTimeCount > 0) {
                    sql.append(" AND bpv.service_status ='ZAI_GUAN' ");
                }
            }

            sql.append(" AND ( ");
            StringBuffer sqlSplice = new StringBuffer();
            String andOr = rules.getConditionType() == 1 ? " OR " : " AND ";
            for (RuleConditionDTO condition : conditions) {
                SysDictData conditionFHCache = DictUtils.getDictData(RulesVal.GUI_ZE_FU_HAO.getCode(), condition.getCondition());
                if (Objects.isNull(conditionFHCache)) {
                    continue;
                }
                // 时间规则
                if (condition.getFieldType().contains(RulesVal.SHI_JIAN.getCode()) || condition.getFieldType().contains(RulesVal.SHI_JIAN_TOUTUO.getCode()) || condition.getFieldType().contains(RulesVal.SHI_JIAN_CUSTOMER.getCode())) {
                    RulesDict rulesDict = rulesDictMap.get(condition.getFieldName());
                    if (Objects.isNull(rulesDict)) {
                        continue;
                    }
                    String cValue = condition.getConditionValue();
                    //获取单位进行处理,单位有年月日（天）时分秒，对单位进行拼接字符串
                    String unit = StrUtil.isBlank(condition.getUnit()) ? "TIAN" : condition.getUnit();
                    sqlSplice.append(andOr);
                    switch (unit) {
                        case "NIAN"://间隔年份
                            if (StringUtils.isNotEmpty(conditionFHCache.getDictLabel()) && StringUtils.isNotEmpty(cValue)) {
                                if (StringUtils.isNotEmpty(rulesDict.getOtherVal()) && rulesDict.getOtherVal().equals("Y")) {
                                    //以当前时间为例，1年之前
                                    sqlSplice.append(" TIMESTAMPDIFF(YEAR,DATE_FORMAT(NOW(), '%Y-%m-%d'),DATE_FORMAT(" + rulesDict.getRuleField() + ", '%Y-%m-%d') ) " + conditionFHCache.getDictLabel() + " " + cValue);
                                } else {
                                    //以当前时间为例，1年之后
                                    // 如果参数里面有负数
                                    if (cValue.contains("-")) {
                                        sqlSplice.append(" TIMESTAMPDIFF(YEAR,DATE_FORMAT(NOW(), '%Y-%m-%d'),DATE_FORMAT(" + rulesDict.getRuleField() + ", '%Y-%m-%d') ) " + conditionFHCache.getDictLabel() + " " + cValue);
                                    } else {
                                        sqlSplice.append(" TIMESTAMPDIFF(YEAR,DATE_FORMAT(" + rulesDict.getRuleField() + ",'%Y-%m-%d'),DATE_FORMAT(NOW(), '%Y-%m-%d')) " + conditionFHCache.getDictLabel() + " " + cValue + " AND TIMESTAMPDIFF(YEAR,DATE_FORMAT(" + rulesDict.getRuleField() + ",'%Y-%m-%d'),DATE_FORMAT(NOW(), '%Y-%m-%d'))  >=0 ");
                                    }
                                }
                            }
                            break;
                        case "YUE"://间隔月份
                            if (StringUtils.isNotEmpty(conditionFHCache.getDictLabel()) && StringUtils.isNotEmpty(cValue)) {
                                //以当前时间为例，1个月之前
                                if (StringUtils.isNotEmpty(rulesDict.getOtherVal()) && rulesDict.getOtherVal().equals("Y")) {
                                    sqlSplice.append(" TIMESTAMPDIFF(MONTH,DATE_FORMAT(NOW(), '%Y-%m-%d'),DATE_FORMAT(" + rulesDict.getRuleField() + ", '%Y-%m-%d') ) " + conditionFHCache.getDictLabel() + " " + cValue);
                                } else {
                                    // 以当前时间为例，1个月之后
                                    // 如果参数里面有负数
                                    if (cValue.contains("-")) {
                                        sqlSplice.append(" TIMESTAMPDIFF(MONTH,DATE_FORMAT(NOW(), '%Y-%m-%d'),DATE_FORMAT(" + rulesDict.getRuleField() + ", '%Y-%m-%d') ) " + conditionFHCache.getDictLabel() + " " + cValue);
                                    } else {
                                        sqlSplice.append(" TIMESTAMPDIFF(MONTH,DATE_FORMAT(" + rulesDict.getRuleField() + ",'%Y-%m-%d'),DATE_FORMAT(NOW(), '%Y-%m-%d')) " + conditionFHCache.getDictLabel() + " " + cValue + " AND TIMESTAMPDIFF(MONTH,DATE_FORMAT(" + rulesDict.getRuleField() + ",'%Y-%m-%d'),DATE_FORMAT(NOW(), '%Y-%m-%d'))  >=0   ");
                                    }
                                }
                            }
                            break;
                        case "TIAN"://间隔天数
                            if (StringUtils.isNotEmpty(conditionFHCache.getDictLabel()) && StringUtils.isNotEmpty(cValue)) {
                                //以当前时间为例，1天之前
                                if (StringUtils.isNotEmpty(rulesDict.getOtherVal()) && rulesDict.getOtherVal().equals("Y")) {
                                    sqlSplice.append(" (TO_DAYS(now()) - TO_DAYS(" + rulesDict.getRuleField() + ") " + conditionFHCache.getDictLabel() + " " + cValue + ") ");
                                } else {
                                    //以当前时间为例，1天之后
                                    // 如果参数里面有负数
                                    if (cValue.contains("-")) {
                                        sqlSplice.append(" (TO_DAYS(" + rulesDict.getRuleField() + ") - TO_DAYS(now()) " + conditionFHCache.getDictLabel() + " " + cValue + ") ");
                                    } else {
                                        sqlSplice.append(" (TO_DAYS(" + rulesDict.getRuleField() + ") - TO_DAYS(now()) " + conditionFHCache.getDictLabel() + " " + cValue + ") AND TO_DAYS(" + rulesDict.getRuleField() + ")-TO_DAYS(now()) >=0 ");
                                    }
                                }
                            }
                            break;
                        default:
                            break;
                    }
                }

                // 业务规则
                else if (condition.getFieldType().contains(RulesVal.YE_WU.getCode()) || condition.getFieldType().contains(RulesVal.YE_WU_TOUTUO.getCode()) || condition.getFieldType().contains(RulesVal.YE_WU_CUSTOMER.getCode()) || condition.getFieldType().contains(RulesVal.XI_TONG.getCode())) {
                    RulesDict rulesF = rulesDictMap.get(condition.getFieldName());
                    if (null != rulesF) {
                        String[] ruleValues = condition.getConditionValue().split(",");
                        String values = "";
                        for (String ruleValue : ruleValues) {
                            SysDictData rulesV = DictUtils.getDictData(rulesF.getRuleValue(), ruleValue);
                            if (null != rulesV) {
                                values += "'" + (StringUtils.isNotEmpty(rulesV.getCssClass()) ? rulesV.getCssClass() : rulesV.getDictValue()) + "',";
                            } else {
                                values += "'" + rulesV + "',";
                            }
                        }

                        values = values.substring(0, values.length() - 1);
                        sqlSplice.append(andOr);
                        if (conditionFHCache.getDictValue().equals("3")) {

                            String undone = "0";
                            String done = "1";
                            String yeJiQueRen = "18";
                            String biaoShuPingShen = "37";
                            String touBiaoFuPan = "14";
                            // 不用排除未到某一节点的项目数据
                            String systemFieldSql = " bpv.id in (SELECT pro.id FROM biz_project_step ps JOIN biz_project pro ON ps.project_id = pro.id WHERE ps.`status` = %s AND ps.step_menu_id = %s ) ";
                            // 除了要判断某一节点是否完成之外，还要排除还没到指定节点的数据
                            String systemFieldDistinctSql = " bpv.id in (SELECT pro.id FROM biz_project pro LEFT JOIN biz_project_step ps ON ps.project_id = pro.id WHERE ((ps.`status` = %s AND ps.step_menu_id = %s) OR (pro.id IN ( SELECT DISTINCT project_id FROM biz_project_step WHERE project_id NOT IN (SELECT project_id FROM biz_project_step WHERE step_menu_id = %s ) OR step_menu_id IS NULL )) OR ps.id IS NULL)) ";
                            // 结项逻辑：完成业绩确认，并且没有完成结项节点，或着完成业绩确认，还没到结项几点
                            String jieXiangSql = " bpv.id in ( SELECT project_id FROM biz_project_step WHERE (step_menu_id = 18 AND status = 1) AND ( (step_menu_id = 40 AND status = 0 ) OR project_id NOT IN  ( SELECT project_id FROM biz_project_step WHERE step_menu_id = 40 AND status = 1 ) OR project_id NOT IN  ( SELECT project_id FROM biz_project_step WHERE step_menu_id = 40 ))) ";

                            // 自定义：如果规则参数里面包含--是否有效
                            if (rulesF.getRuleField().contains("expire")) {
                                sqlSplice.append(rulesF.getRuleField() + " = " + (ObjectUtil.equals(values, "'SHI'") ? "'YOU_XIAO'" : "'YI_SHI_XIAO'"));
                            }
                            // 自定义：系统字段：业绩确认
                            else if (rulesF.getRuleField().contains("YE_JI_QUE_REN")) {
                                // 未完成
                                if (ObjectUtil.equals(values, "'0'")) {
                                    sqlSplice.append(String.format(systemFieldSql, undone, yeJiQueRen));
                                }
                                // 已完成
                                else {
                                    sqlSplice.append(String.format(systemFieldSql, done, yeJiQueRen));
                                }
                            }
                            // 自定义：系统字段：标书评审
                            else if (rulesF.getRuleField().contains("BIAO_SHU_PING_SHEN")) {
                                // 未完成
                                if (ObjectUtil.equals(values, "'0'")) {
                                    sqlSplice.append(String.format(systemFieldDistinctSql, undone, biaoShuPingShen, biaoShuPingShen));
                                }
                                // 已完成
                                else {
                                    sqlSplice.append(String.format(systemFieldDistinctSql, done, biaoShuPingShen, biaoShuPingShen));
                                }
                            }
                            // 自定义：系统字段：投标复盘
                            else if (rulesF.getRuleField().contains("TOU_BIAO_FU_PAN")) {
                                // 未完成
                                if (ObjectUtil.equals(values, "'0'")) {
                                    sqlSplice.append(String.format(systemFieldDistinctSql, undone, touBiaoFuPan, touBiaoFuPan));
                                }
                                // 已完成
                                else {
                                    sqlSplice.append(String.format(systemFieldDistinctSql, done, touBiaoFuPan, touBiaoFuPan));
                                }
                            }
                            // 自定义：系统字段：结项
                            else if (rulesF.getRuleField().contains("JIE_XIANG")) {
                                // 未完成
                                if (ObjectUtil.equals(values, "'0'")) {
                                    sqlSplice.append(jieXiangSql);
                                }
                            } else {
                                sqlSplice.append(rulesF.getRuleField() + " IN (" + values + ")");
                            }
                        } else if (conditionFHCache.getDictValue().equals("7")) {
                            sqlSplice.append(rulesF.getRuleField() + " NOT IN (" + values + ")");
                        }
                    }
                }
            }
            //查询条件SQL截取
            String sqlSpliceStr = sqlSplice.toString();
            if (StrUtil.isNotBlank(sqlSpliceStr)) {
                sqlSpliceStr = sqlSpliceStr.replaceFirst(andOr, "");
            } else {
                sqlSpliceStr = " 1=1 ";
            }
            //查表和条件拼接
            sql.append(sqlSpliceStr);
            sql.append(") ");
            //最终形成SQL  例如
            // SELECT * FROM  aa where 1=1 and (project.service_status ='ZAI_GUAN') and(or(a=1) or (b=2))
            //或者 SELECT * FROM  aa where 1=1  and(or(a=1) or (b=2)) and c=3
            log.info("定时任务执行规则，执行的具体SQL={}", sql.toString());
            List<Map<String, Object>> baseEntities = commonMapper.listBizData(sql.toString());
            data.put("baseEntities", baseEntities);
            data.put("associationTable", associationTable);
            return data;
        }
        data.put("baseEntities", Lists.newArrayList());
        data.put("associationTable", associationTable);
        return data;
    }

    /**
     * 执行动作
     *
     * @param rules
     * @param rulesModeName
     * @param datas
     * @param rulesDictMap
     */
    private void runAction(Rules rules, RulesDict rulesModeName, List<Map> datas, Map<String, RulesDict> rulesDictMap, String associationTable) {
        List<RuleActionDTO> actions = JSON.parseArray(rules.getAction(), RuleActionDTO.class);

        List<ActionMessage> amlist = Lists.newArrayList();
        List<ActionValue> avlist = Lists.newArrayList();

        // 解析数据，生成变更sql， 消息通知
        for (RuleActionDTO action : actions) {
            // 消息通知
            if (action.getActionType().equals(RulesVal.XIAO_XI_TONG_ZHI.getCode()) || action.getActionType().equals(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode()) || action.getActionType().equals(RulesVal.XIAO_XI_TONG_ZHI_CUSTOMER.getCode()) || action.getActionType().equals(RulesVal.XIAO_XI_TONG_ZHI_OA_TODO_REMIND.getCode())) {
                if (CollectionUtil.isEmpty(action.getSendChannels()) || CollectionUtil.isEmpty(action.getSendObjects())) {
                    continue;
                }

                ActionMessage am = new ActionMessage();
                am.title = action.getMessageTitle();
                am.content = action.getMessageContent();
                am.type = action.getSendType().equals(1);
                am.startTime = action.getStartTime();
                am.sendTime = action.getSendTime();
                am.sendChannels = String.join(",", action.getSendChannels());

                // 时间单位
                SysDictData dictData = DictUtils.getDictData(RulesVal.SHI_JIAN_ZHOU_QI.getCode(), action.getSendUnit());
                if (null != dictData && StringUtils.isNotEmpty(dictData.getCssClass())) {
                    am.sendUnit = Integer.valueOf(dictData.getCssClass());
                } else {
                    am.sendUnit = 1;
                }

                // 消息发送对象
                for (String sendObject : action.getSendObjects()) {
                    RulesSendObject rso = RulesSendObject.getByCode(sendObject.replaceAll((RulesVal.XIAO_XI_TONG_ZHI.getCode() + "_"), ""));
                    if (sendObject.contains(RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode())) {
                        rso = RulesSendObject.getByCode(sendObject.replaceAll((RulesVal.XIAO_XI_TONG_ZHI_OPERATE.getCode() + "_"), ""));
                    }
                    if (sendObject.contains(RulesVal.XIAO_XI_TONG_ZHI_CUSTOMER.getCode())) {
                        rso = RulesSendObject.getByCode(sendObject.replaceAll((RulesVal.XIAO_XI_TONG_ZHI_CUSTOMER.getCode() + "_"), ""));
                    }
                    if (sendObject.contains(RulesVal.XIAO_XI_TONG_ZHI_OA_TODO_REMIND.getCode())) {
                        rso = RulesSendObject.getByCode(sendObject.replaceAll((RulesVal.XIAO_XI_TONG_ZHI_OA_TODO_REMIND.getCode() + "_"), ""));
                    }
                    if (null != rso) {
                        am.sendObjects.add(rso);
                    }
                }
                amlist.add(am);
            }

            // 变更枚举值
            else if (action.getActionType().equals(RulesVal.BIAN_GENG_ZHI.getCode())) {
                RulesDict rulesF = rulesDictMap.get(action.getUpdateField());
                SysDictData rulesV = DictUtils.getDictData(rulesF.getRuleValue(), action.getUpdateValue());
                String tableN = rulesModeName.getRuleField();

                ActionValue av = new ActionValue();
                av.updateField = rulesF.getRuleField();

                // 关联处理值(关联线索，关联商机等)
                RulesDict rDict = rulesDictMap.get(rulesF.getRuleField());
                if (null != rDict && rDict.getRuleGroup().equals(RulesVal.RULES_MODE_NAME.getCode())) {
                    tableN = rDict.getRuleField();
                    av.updateFieldId = CamelUtils.camelToUndeline(rDict.getRuleValue()) + "_id";
                    av.updateField = CamelUtils.camelToUndeline(rDict.getRuleValue()) + "_status";
                    av.isJoin = true;
                }

                // 特殊value(如状态等)
                av.updateValue = StringUtils.isNotEmpty(rulesV.getCssClass()) ? rulesV.getCssClass() : rulesV.getDictValue();
                String set = av.updateField + " = '" + av.updateValue + "'";

                if (StringUtils.isNotEmpty(rulesV.getListClass())) {
                    String[] split = rulesV.getListClass().split("@@@");
                    if (split.length == 2) {
                        set += ", " + split[0] + " = " + split[1];
                    }
                }

                // 指定table名称
                String updateFieldName = "id";
                if (StringUtils.isNotEmpty(rulesF.getOtherVal())) {
                    String[] split = rulesF.getOtherVal().split("@@@");
                    if (split.length == 2) {
                        tableN = split[0];
                        updateFieldName = split[1];
                    }
                }

                av.sql = "UPDATE " + tableN + " SET " + set + " WHERE " + updateFieldName + " = #{id} ";
                av.updateFieldLabel = rulesF.getRuleDesc();
                av.updateFieldDict = rulesF.getRuleValue();
                av.updateValueLabelType = StringUtils.isNotEmpty(rulesV.getCssClass()) ? 1 : 2;

                avlist.add(av);
            }
        }

        for (Map<String, Object> data : datas) {
            if (data.containsKey("id") && StringUtils.isNotEmpty(data.get("id").toString())) {
                getThis().processData(rulesModeName.getRuleValue(), data, amlist, avlist, rules, associationTable);
            }
        }
    }

    /**
     * * 1、执行消息通知
     * * 2、执行值变更
     * * 3、记录数据变更日志
     * * 4、记录规则变更日志
     *
     * @param modeName
     * @param data
     * @param amlist
     * @param avlist
     * @param rules
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void processData(String modeName, Map<String, Object> data, List<ActionMessage> amlist, List<ActionValue> avlist, Rules rules, String associationTable) {
        Long id = this.getObjectId(data, "id");
        Long associationBusinessId = this.getObjectId(data, "associationBusinessId");
        if (null == id) {
            return;
        }

        // 变更值
        for (ActionValue actionValue : avlist) {
            Long dataId = id;
            if (actionValue.isJoin && null == (dataId = this.getObjectId(data, actionValue.updateFieldId))) {
                continue;
            }

            String updField = StringUtils.nvEmpty(data.get(actionValue.updateField));
            String bValue = "";
            SysDictData dictData = null;
            if (actionValue.updateValueLabelType == 1) {
                dictData = DictUtils.getDictDataBycc(actionValue.updateFieldDict, updField);
            } else if (actionValue.updateValueLabelType == 2) {
                dictData = DictUtils.getDictData(actionValue.updateFieldDict, updField);
            }

            bValue = (null == dictData) ? BizConstant.BIZ_LOG_NULL : dictData.getDictLabel();

            // 执行变更，记录变更值
            int re = commonMapper.updateBiz(actionValue.sql.replace("#{id}", dataId.toString()));
            if (re > 0) {
                log.info("变更记录执行sql==>{}", actionValue.sql.replace("#{id}", dataId.toString()));
                // 记录变更日志
                operLogService.ruleLogBySystem(
                        modeName,
                        dataId,
                        UpdateLog.builder()
                                .fieldName(actionValue.updateFieldLabel)
                                .valueBefore(bValue)
                                .valueAfter(StringUtils.isNotEmpty(actionValue.updateValue) ? actionValue.updateValue : "")
                                .build()
                );
            }
        }

        // 发送消息
        for (ActionMessage actionMessage : amlist) {
            String amUnid = MD5Tool.md5(JSON.toJSONString(actionMessage));
            Set<Long> ids = Sets.newHashSet();
            // 查询发送对象
            for (RulesSendObject sendObject : actionMessage.sendObjects) {
                log.info("定时任务执行规则，发送给的消息的具体对象={}", sendObject.getDesc());
                switch (sendObject) {
                    case GUI_SHU_REN: // 归属人
                        Long attributorUserId = this.getObjectId(data, sendObject.getTableFieldName());
                        if (null != attributorUserId) {
                            ids.add(attributorUserId);
                        }
                        break;
                    case TUO_ZHAN_GUI_SHU_REN: // 拓展归属人
                        Long attributorUserIdtz = this.getObjectId(data, sendObject.getTableFieldName());
                        if (null != attributorUserIdtz) {
                            ids.add(attributorUserIdtz);
                        }
                        break;
                    case TUO_HOU_FU_ZE_REN:// 拓后负责人
                        Long principalId = this.getObjectId(data, sendObject.getTableFieldName());
                        if (null != principalId) {
                            ids.add(principalId);
                        }
                        break;
                    case XIANG_MU_CHUANG_JIAN_REN://项目创建人
                        Long createUserId = this.getObjectId(data, sendObject.getTableFieldName());
                        if (null != createUserId) {
                            ids.add(createUserId);
                        }
                        break;
                    case FANG_AN_SHI_SHI_REN://干预方案实施人
                        Long userId = this.getObjectId(data, sendObject.getTableFieldName());
                        if (null != userId) {
                            ids.add(userId);
                        }
                        break;
                    case FU_ZE_REN:
                        Long deptId = this.getObjectId(data, sendObject.getTableFieldName());
                        if (null != deptId) {
                            List<UserVO> users = userService.getUserByDeptId(deptId);
//                            List<UserRoleDTO> users = userService.findSuperiorsRolePermsUser(deptId, DataRole.SHOW_ALL.getRoleKey(), DataRole.SHOW_SUB.getRoleKey());
                            ids.addAll(users.stream().map(UserVO::getUserId).collect(Collectors.toList()));
                        }
                        break;
                    case CAN_YU_REN:
                        List<BizUserBaseEntity> bizUsers = commonMapper.listBizUser(
                                CamelUtils.camelToUndeline(modeName),
                                Arrays.asList(new Long[]{id}));
                        if (!bizUsers.isEmpty()) {
                            ids.addAll(bizUsers.stream().map(BizUserBaseEntity::getUserId).collect(Collectors.toList()));
                        }
                        break;
                    case ZONG_BU_YUN_YING_BU_GUAN_LI_FU_ZE_REN:
                        //查出运营部管理负责人角色的数据
                        List<Long> OperateManageUserId = userService.selectHeadOperateRoleUserId(deptName);
                        ids.addAll(OperateManageUserId);
                        break;
                    case KE_HU_WEI_HU_REN:
                        //客户维护人--todo
                        Long maintenanceUserId = this.getObjectId(data, sendObject.getTableFieldName());
                        if (null != maintenanceUserId) {
                            ids.add(maintenanceUserId);
                        }
                        break;
                    default:
                        break;
                }
            }

            // 一次性 OR 周期发送
            if (CollectionUtil.isNotEmpty(ids)) {
                log.info("定时任务执行规则，发送给的消息的人员ID列表={}", ids);
                boolean isTime = false;
                int type = actionMessage.sendTime * actionMessage.sendUnit;
                for (Long aLong : ids) {
                    Integer count;
                    // 周期发送的数据
                    if (!actionMessage.type) {
                        Date date = DateUtils.addDays(new Date(), -type);
                        log.info("定时任务执行规则，进入周期发送,具体查询条件：getRecordId={},getRulesId={},getMessageId={},getUserId={},getAssociationTable={},getAssociationBusinessId={},getSendTime={}",
                                id, rules.getId(), amUnid, aLong, associationTable, associationBusinessId, DateUtil.formatDateTime(date)
                        );

                        // 查询此条数据是否已经发送过
                        count = rulesMessageLogMapper.selectCount(
                                Wrappers.<RulesMessageLog>lambdaQuery()
                                        .eq(RulesMessageLog::getRecordId, id)
                                        .eq(RulesMessageLog::getRulesId, rules.getId())
                                        .eq(RulesMessageLog::getMessageId, amUnid)
                                        .eq(RulesMessageLog::getUserId, aLong)
                                        .ge(RulesMessageLog::getSendTime, date)
                                        .and(StrUtil.isNotBlank(associationTable), query -> query.eq(RulesMessageLog::getAssociationTable, associationTable)
                                                .eq(RulesMessageLog::getAssociationBusinessId, associationBusinessId))
                        );

                        // 判断当前时间是否和任务开始时间相等
                        isTime = isTime(actionMessage);
                        if (!isTime) {
                            System.out.println("当前时间和配置时间不一致，任务不执行");
                        } else {
                            // 如果是项目OA待办提醒
                            if (ObjectUtil.equals(RulesVal.OA_TODO_REMIND.getCode(), rules.getModeName())) {
                                // 处理-项目合同到期监管提醒、开标评审监管提醒、投标复盘监管提醒、项目结项移交监管提醒、长期未维护项目监管提醒，并将count +1,不往下走
                                count = handleCycleRule(actionMessage, rules, aLong, count, data, type, 1);
                            }
                            // 如果是客户
                            if (ObjectUtil.equals(RulesVal.TOUTUO_CUSTOMER.getCode(), rules.getModeName())) {
                                // 处理-客户规则
                                count = handleCycleRule(actionMessage, rules, aLong, count, data, type, 3);
                            }
                        }
                    } else {
                        log.info("定时任务执行规则，进入发送一次,具体查询条件：getRecordId={},getRulesId={},getMessageId={},getAssociationTable={},getAssociationBusinessId={},getUserId={}",
                                id, rules.getId(), amUnid, associationTable, associationBusinessId, aLong
                        );
                        // 查询此条数据是否已经发送过
                        count = rulesMessageLogMapper.selectCount(
                                Wrappers.<RulesMessageLog>lambdaQuery()
                                        .eq(RulesMessageLog::getRecordId, id)
                                        .eq(RulesMessageLog::getRulesId, rules.getId())
                                        .eq(RulesMessageLog::getMessageId, amUnid)
                                        .eq(RulesMessageLog::getUserId, aLong)
                                        .and(StrUtil.isNotBlank(associationTable), query -> query.eq(RulesMessageLog::getAssociationTable, associationTable)
                                                .eq(RulesMessageLog::getAssociationBusinessId, associationBusinessId))
                        );
                    }
                    if (count == 0) {
                        // 变量替换 {lead_name}, {opportunity_name},  {project_name}, {customer_name}
                        String msg = actionMessage.content;
                        int start, end;
                        if (StringUtils.isNotEmpty(msg)
                                && (start = msg.indexOf("{")) != -1
                                && (end = msg.indexOf("}")) != -1) {
                            String field = msg.substring(start, end + 1);
                            Object val = data.get(field.replace("{", "").replace("}", ""));
                            if (null != val && val instanceof String) {
                                msg = msg.replace(field, (String) val);
                            }
                        }

                        UserVO user = userService.getUser(aLong);
                        if (ObjectUtil.isNotEmpty(user)) {
                            JSONObject requestBody = new JSONObject();
                            requestBody.put("receive_id", user.getUserName());
                            requestBody.put("msg_type", "text");
                            JSONObject content = new JSONObject();
                            content.put("text", msg);
                            requestBody.put("content", content.toString());
                        }

                        RulesMessageLog rulesMessageLog = new RulesMessageLog();
                        rulesMessageLog.setRecordId(id);
                        rulesMessageLog.setRulesId(rules.getId());
                        rulesMessageLog.setMessageId(amUnid);
                        rulesMessageLog.setSendTime(new Date());
                        rulesMessageLog.setUserId(aLong);
                        rulesMessageLog.setAssociationTable(associationTable);
                        rulesMessageLog.setAssociationBusinessId(associationBusinessId);
                        rulesMessageLogMapper.insert(rulesMessageLog);

                        log.info("定时任务执行规则,发送消息===>数据模块：{}, 数据id：{}  {}", modeName, id, rulesMessageLog);
                    }
                }

                // 如果当前时间触发了任务执行，将规则的任务开始时间提到下一个周期
                if (isTime) {
                    List<RuleActionDTO> actionsOld = JSON.parseArray(rules.getAction(), RuleActionDTO.class);
                    List<RuleActionDTO> actionsNew = Lists.newArrayList();
                    if (ObjectUtil.isNotEmpty(actionsOld)) {
                        for (RuleActionDTO dto : actionsOld) {
                            Date date = DateUtils.addDays(new Date(), type);
                            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            dto.setStartTime(dateFormatter.format(date));
                            actionsNew.add(dto);
                        }
                    }
                    Rules ruleInfo = new Rules();
                    ruleInfo.setId(rules.getId());
                    ruleInfo.setAction(JSON.toJSONString(actionsNew));
                    this.updateById(ruleInfo);
                }
            }
        }
    }

    private boolean isTime(ActionMessage actionMessage) {
        // 判断任务开始时间是否符合
        String startTime = actionMessage.getStartTime();
        String nowTime = StringUtils.EMPTY;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            // 把当前时间的秒数置0，方便时间判断
            LocalDateTime currentTime = LocalDateTime.now().withSecond(0);
            nowTime = formatter.format(currentTime);

            // 把任务开始时间的秒数置0，方便时间判断
            LocalDateTime startTimeLocal = LocalDateTime.parse(startTime, formatterStart).withSecond(0);
            startTime = formatter.format(startTimeLocal);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        System.out.println("当前时间：" + nowTime + "和配置时间：" + startTime);
        return startTime.equals(nowTime);
    }

    // 处理-客户规则，并将count +1,不往下走
    private Integer handleCycleRule(ActionMessage actionMessage, Rules rules, Long userId, Integer count, Map<String, Object> data, Integer type, Integer moduleType) {
        System.out.println("开始处理规则消息提醒--");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 把任务开始时间的秒数置0，方便时间判断
        LocalDateTime startTimeLocal = LocalDateTime.parse(actionMessage.getStartTime(), formatterStart).withSecond(0);
        String startTime = formatter.format(startTimeLocal);

        // 业务id
        Long serviceId = Long.valueOf(data.get("id").toString());

        // 查询oa发送代办日志表，是否已经发送过代办
        List<OaMessageLog> logList = Lists.newArrayList();
        LambdaQueryWrapper<OaMessageLog> messageLogQw = new LambdaQueryWrapper<>();
        messageLogQw.eq(OaMessageLog::getModuleId, rules.getId());
        messageLogQw.eq(OaMessageLog::getModuleType, moduleType);
        messageLogQw.eq(OaMessageLog::getUserId, userId);
        messageLogQw.eq(OaMessageLog::getServiceId, serviceId);
        if (ObjectUtil.equals(moduleType, 3)) {
            logList = null;
        } else {
            logList = iOaMessageLogService.list(messageLogQw);
        }
        if (ObjectUtil.isNotEmpty(logList)) {
            System.out.println("规则名称：" + rules.getRuleName() + "，userId：" + userId + "业务id：" + Long.valueOf(data.get("id").toString()) + "，存在相同的待办，不重复发送");
            return ++count;
        }
        // 处理oa消息标题
        String title = actionMessage.getTitle();
        // 处理oa消息内容
        String content = actionMessage.getContent();

        if (ObjectUtil.equals(moduleType, 1)) {
            if (actionMessage.getTitle().contains("@projectName")) {
                title = actionMessage.getTitle().replace("@projectName", data.get("project_name").toString());
            }
            if (actionMessage.getContent().contains("@projectName")) {
                content = actionMessage.getContent().replace("@projectName", data.get("project_name").toString());
            }
            // 发送oa代办
            sendOaTodoMessage(rules, title, content, userId, serviceId, startTime, 1);
        } else if (ObjectUtil.equals(moduleType, 3)) {
            if (actionMessage.getTitle().contains("@CustomerName")) {
                title = actionMessage.getTitle().replace("@CustomerName", data.get("customer_name").toString());
            }
            if (actionMessage.getContent().contains("@CustomerName")) {
                content = actionMessage.getContent().replace("@CustomerName", data.get("customer_name").toString());
            }
            // 发送oa代办
            sendOaTodoMessage(rules, title, content, userId, serviceId, startTime, 3);
        }
        return ++count;
    }

    //发送OA待办消息：客户管理

    public void sendOaTodoMessageForCustomer(Rules rules, String title, String content, Long userId, Long customerId, String date) {
        SysUser user = userMapper.selectById(userId);
        String phone = user.getPhonenumber();
        String modelName = rules.getRuleName();
        // 记录发送oa待办日志
        OaMessageLog messageLog = new OaMessageLog();
        messageLog.setModule(modelName);
        messageLog.setModuleId(rules.getId());
        messageLog.setModuleType(3);
        messageLog.setUserId(user.getUserId());
        messageLog.setTitle(title);
        messageLog.setContent(content);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 把任务开始时间的秒数置0，方便时间判断
            LocalDateTime startTimeLocal = LocalDateTime.parse(date, formatterStart).withSecond(0);
            nowTime = sdf.parse(formatter.format(startTimeLocal).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        messageLog.setCreateTime(nowTime);
        messageLog.setStatus(1);
        messageLog.setServiceId(customerId);
        iOaMessageLogService.save(messageLog);
        //发送待办通知的url
        String todoUrl = oaProperties.getTodoUrl();
        //客户管理-客户明细-客户动态url
        String customerUrl = oaProperties.getCustomerUrl();
        //https://tt-uat.gem-flower.com/#/innerPage/customerInfo?id=2049&tab=3
        OaTodo todo = new OaTodo();
        todo.setAppName("TOUTUO");
        todo.setModelName(modelName);
        todo.setModelId(rules.getId().toString());
        todo.setSubject(title);
        todo.setLink(String.format(customerUrl, customerId));
        todo.setMobileLink(String.format(customerUrl, customerId));
        todo.setPadLink(String.format(customerUrl, customerId));
        todo.setType(1);
        Map<String, String> targets = new HashMap<>();
        targets.put("MobileNo", phone);
        todo.setTargets(targets);
        todo.setDocCreator(targets);
        todo.setCreateTime(DateUtils.getTime());
        String str = com.alibaba.fastjson2.JSON.toJSONString(todo);
        System.out.println("客户维护-发送待办消息:" + str);
        String body = HttpUtil.createPost(todoUrl).contentType("application/json").body(com.alibaba.fastjson2.JSON.toJSONString(todo)).execute().body();
        System.out.println("客户维护-发送待办消息返回结果:" + body);
        messageLog.setMessage(JSON.toJSONString(todo));
        iOaMessageLogService.updateById(messageLog);
    }


    public void sendOaTodoMessage(Rules rules, String title, String content, Long userId, Long projectId, String date, Integer type) {

        SysUser user = userMapper.selectById(userId);
        String phone = user.getPhonenumber();
        String modelName = rules.getRuleName();
        String detailUrl = oaProperties.getRuleDetailUrl();
        // 记录发送oa待办日志
        OaMessageLog messageLog = new OaMessageLog();
        messageLog.setModule(modelName);
        messageLog.setModuleId(rules.getId());
        messageLog.setModuleType(type);
        messageLog.setUserId(user.getUserId());
        messageLog.setTitle(title);
        messageLog.setContent(content);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:00");
        DateTimeFormatter formatterStart = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Date nowTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // 把任务开始时间的秒数置0，方便时间判断
            LocalDateTime startTimeLocal = LocalDateTime.parse(date, formatterStart).withSecond(0);
            nowTime = sdf.parse(formatter.format(startTimeLocal).toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        messageLog.setCreateTime(nowTime);
        messageLog.setStatus(1);
        messageLog.setServiceId(projectId);
        iOaMessageLogService.save(messageLog);

        //type = 1项目 ，3客户
        String modeule = "Project";
        if (type.equals(3))
            modeule = "Customer";
        saveSysMessage(type, userId, projectId, title, content, modeule);

        // 发送待办通知的url
        String todoUrl = oaProperties.getTodoUrl();
        OaTodo todo = new OaTodo();
        todo.setAppName("TOUTUO");
        todo.setModelName(modelName);
        todo.setModelId(messageLog.getId().toString());
        todo.setSubject(title);
        todo.setLink(String.format(detailUrl, messageLog.getId()));
        todo.setMobileLink(String.format(detailUrl, messageLog.getId()));
        todo.setPadLink(String.format(detailUrl, messageLog.getId()));
        todo.setType(1);
        Map<String, String> targets = new HashMap<>();
        targets.put("MobileNo", phone);
        todo.setTargets(targets);
        todo.setDocCreator(targets);
        todo.setCreateTime(DateUtils.getTime());
        String str = com.alibaba.fastjson2.JSON.toJSONString(todo);
        System.out.println(modelName + "发送待办消息传递参数:" + str);
        String body = HttpUtil.createPost(todoUrl).contentType("application/json").body(com.alibaba.fastjson2.JSON.toJSONString(todo)).execute().body();
        System.out.println(modelName + "发送待办消息返回结果:" + body);
        messageLog.setMessage(JSON.toJSONString(todo));
        iOaMessageLogService.updateById(messageLog);
    }

    public void saveSysMessage(Integer type, Long userId, Long projectId, String title, String content, String modeule) {
        try {
            System.out.println("oa待办信息保存系统信息开始");
            SysMessage sysMessage = new SysMessage();
            sysMessage.setCreateTime(new Date());
            sysMessage.setMessageType("DEFAULT");
            if (type.equals(1))
                sysMessage.setModule("Project");
            sysMessage.setUserId(userId);
            sysMessage.setOpenStatus(0);
            sysMessage.setModuleId(projectId);
            sysMessage.setTitle(title);
            sysMessage.setMessage(content);
            System.out.println("oa待办信息保存系统信息内容：" + JSON.toJSONString(sysMessage));
            sendMessageService.sendMessage(userId, projectId, title, content, modeule, "XI_TONG_TONG_ZHI");
            System.out.println("oa待办信息保存系统信息结束");
        } catch (Exception ex) {
            System.out.println("oa待办信息保存系统信息异常");
        }
    }

    /**
     * 获取指定id的数据
     *
     * @param data
     * @param key
     * @return
     */
    private Long getObjectId(Map<String, Object> data, String key) {
        try {
            Object vObje;
            if (!data.containsKey(key) || null == (vObje = data.get(key))) {
                return null;
            }
            return Long.valueOf(vObje.toString());
        } catch (Exception e) {
            log.error("{}, {}", e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    private RulesServiceImpl getThis() {
        return SpringUtils.getBean(this.getClass());
    }


    /**
     * 动作发送消息
     */
    @Data
    class ActionMessage {
        private String title;
        private String content;
        private boolean type;
        private Integer sendTime;
        private String startTime;
        private Integer sendUnit;
        private String sendChannels;
        private List<RulesSendObject> sendObjects = Lists.newArrayList();
    }

    /**
     * 动作变更值
     */
    class ActionValue {
        // 修改的字段
        private String updateField;

        // 修改的字段描述
        private String updateFieldLabel;

        // 修改的字段值
        private String updateValue;

        private String updateFieldId;

        // 修改字段描述
        private Integer updateValueLabelType;

        private String updateFieldDict;

        private String sql;

        private boolean isJoin = false;
    }

}
