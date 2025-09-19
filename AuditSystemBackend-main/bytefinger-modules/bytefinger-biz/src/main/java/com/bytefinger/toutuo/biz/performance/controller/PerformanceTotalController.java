//package com.bytefinger.toutuo.biz.performance.controller;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.bytefinger.common.core.web.controller.BaseController;
//import com.bytefinger.common.core.web.domain.vo.AjaxResult;
//import com.bytefinger.common.core.web.domain.vo.DeptVO;
//import com.bytefinger.common.das.domain.vo.QueryPage;
//import com.bytefinger.toutuo.biz.brokerage.domain.ProjectBrokerage;
//import com.bytefinger.toutuo.biz.brokerage.mapper.ProjectBrokerageMapper;
//import com.bytefinger.toutuo.biz.common.mapper.UserMapper;
//import com.bytefinger.toutuo.biz.performance.domain.dto.ActualInDTO;
//import com.bytefinger.toutuo.biz.performance.domain.vo.ActualInTotalVO;
//import com.bytefinger.toutuo.biz.performance.domain.vo.HeadStatisticalVO;
//import com.bytefinger.toutuo.biz.performance.domain.vo.IndivualUserVO;
//import com.bytefinger.toutuo.biz.performance.domain.vo.PerformanceVO;
//import com.bytefinger.toutuo.biz.performance.manager.PerformanceManager;
//import com.bytefinger.toutuo.biz.performance.mapper.PerformanceMapper;
//import com.bytefinger.toutuo.biz.performance.service.IActualInService;
//import com.bytefinger.toutuo.biz.performance.service.IActualOutService;
//import com.bytefinger.toutuo.biz.performance.service.IBudgetInService;
//import com.bytefinger.toutuo.biz.performance.service.IBudgetOutService;
//import com.bytefinger.toutuo.biz.project.domain.Project;
//import com.bytefinger.toutuo.biz.project.domain.ProjectAgreement;
//import com.bytefinger.toutuo.biz.project.service.IProjectAgreementService;
//import com.bytefinger.toutuo.biz.project.service.IProjectContractService;
//import com.bytefinger.toutuo.biz.user.mapper.UserMapper;
//import com.google.common.collect.Maps;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.compress.utils.Lists;
//import org.springframework.web.bind.annotation.*;
//
//import java.math.BigDecimal;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Api(tags = "城市业绩动态表")
//@AllArgsConstructor
//@RestController
//@RequestMapping("/performance")
//public class PerformanceTotalController extends BaseController {
//
//    private final IBudgetInService budgetInYearService;
//
//    private final IBudgetOutService budgetOutYearService;
//
//    private final PerformanceManager performanceManager;
//
//    private final UserMapper userMapper;
//    private final IActualOutService actualOutYearService;
//
//    private final IActualInService actualInService;
//
//    private  final IProjectContractService projectContractService;
//
//    private final PerformanceMapper performanceMapper;
//
//
//    /**
//     * 城市动态业绩统计
//     * @param level
//     * @param recordId
//     * @param currYear
//     * @param actualInDTO
//     * @return
//     */
//    @ApiOperation(value = "城市动态业绩表统计")
//    @PostMapping("/total/{level}/{recordId}/{currYear}")
//    public AjaxResult getByEdit(@PathVariable("level") Integer level, @PathVariable("recordId") Long recordId, @PathVariable("currYear") Integer currYear,@RequestBody ActualInDTO actualInDTO) {
//        if (!performanceManager.dataRole(recordId, level)) {
//            return AjaxResult.error("您没有查看权限");
//        }
//        Map<String, Object> reMap = Maps.newHashMap();
//        PerformanceVO budgetIn = budgetInYearService.getOrUpdate(level, recordId, currYear, null);//预算收入
//        reMap.put("budgetIn",budgetIn);//预算收入
//        List<ActualInTotalVO> actualInDTOList =  actualInService.total(actualInDTO);//实际收入
//        reMap.put("actualInDTOList",actualInDTOList);//实际收入
//        PerformanceVO budgetOut = budgetOutYearService.getOrUpdate(level, recordId, currYear, null);
//        reMap.put("budgetOut",budgetOut);//预算支出
//        PerformanceVO budgetVO = actualOutYearService.getOrUpdate(level, recordId, currYear, null);
//        reMap.put("budgetVO",null == budgetVO ? new PerformanceVO() : budgetVO);//实际支出
//        return success(reMap);
//    }
//
//    /**
//     *
//     * @param queryPage
//     * @return
//     */
//    @ApiOperation(value = "个人业绩列表")
//    @PostMapping("/individualList")
//    public AjaxResult individualList(@RequestBody QueryPage queryPage) {
//        Long canyurenId = (Long) queryPage.getParams().get("canyurenId");
//        List<Long> childDeptIds = Lists.newArrayList();
//        if (!queryPage.getParams().containsKey("deptId")) {
//           return error("请选择部门") ;
//        }
//        Integer deptId = (Integer) queryPage.getParams().get("deptId");
//        if (null != deptId && deptId > 0) {
//            queryPage.getParams().remove("deptId");
//            List<DeptVO> depts = userMapper.getChildDeptIds(Integer.toUnsignedLong(deptId));
//            for (DeptVO dept: depts) {
//                childDeptIds.add(dept.getDeptId());
//            }
//        }
//        Page<IndivualUserVO> page = userMapper.pageUsersByDeptIds(queryPage.toPage(),queryPage.getWrapperByPrefix("su."),queryPage.getDbParams(),childDeptIds);
//                for (IndivualUserVO record : page.getRecords()) {
//                    record.setDeptName(userMapper.getDept(record.getDeptId()).getDeptName());
//                    //查询个人业绩
//                    Long userId = record.getUserId();
//                    List<Map<String, Object>> projectBrokerageUsers = projectBrokerageMapper.getProjectBrokerageProportion(userId);
//                    if (projectBrokerageUsers.size() == 0) {
//                        record.setIndividualAmount(BigDecimal.valueOf(0));
//                    }else {
//                        BigDecimal individualAmount = BigDecimal.valueOf(0);
//                        projectBrokerageUsers = projectBrokerageUsers.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.get("projectId") + "#"))), ArrayList::new));
//                        for (int i = 0; i < projectBrokerageUsers.size(); i++) {
//                            Long projectId = (Long) projectBrokerageUsers.get(i).get("projectId");
//                            String allocationQuota = (String) projectBrokerageUsers.get(i).get("allocationQuota");
//                            ProjectAgreement project = projectAgreementService.getByProjectId(projectId);
//                            if (project.getAmountTexIncome()!=null){
//                                ProjectBrokerage BrokerageSent = projectBrokerageMapper.getApplyAmount(projectId,"2");
//                                BigDecimal amountTexIncome = project.getAmountTexIncome().multiply(BigDecimal.valueOf(0.015));
//                                BigDecimal applyAmount = BigDecimal.valueOf(BrokerageSent.getApplyAmount());//已经发
//                                BigDecimal brokerageBalance = amountTexIncome.subtract(applyAmount);//结余佣金
//                                BigDecimal allocation = new BigDecimal(allocationQuota).divide(BigDecimal.valueOf(100));
//                                BigDecimal  amount = brokerageBalance.multiply(allocation);
//                                individualAmount = individualAmount.add(amount);
//                            }
//                        }
//                        record.setIndividualAmount(individualAmount);
//                    }
//                }
//
//        return success(page);
//    }
//    @ApiOperation(value = "归属于我的项目情况统计")
//    @GetMapping("getProjectIdByUser/{currYear}/{userId}")
//    public  AjaxResult getProjectIdByUser(@PathVariable("currYear") Integer currYear , @PathVariable("userId") Long userId){
//        HeadStatisticalVO StatisticalAgreement = performanceMapper.StatisticalAgreement(userId,currYear);
//        Map<String, BigDecimal> reMap = Maps.newHashMap();
//        reMap.put("projectBrokerageTotalAmount",null == StatisticalAgreement ? BigDecimal.valueOf(0.00) : StatisticalAgreement.getAgreementAmount().multiply(BigDecimal.valueOf(0.015)));
//        List<Long> projectIds = Lists.newArrayList();
//        List<Project> projects = performanceMapper.getProjectIdByUser(currYear,userId);
//        for (Project project : projects) {
//            projectIds.add(project.getId());
//        }
//        if (projectIds.size()==0){
//            reMap.put("applyAmountSUM",BigDecimal.valueOf(0.00));
//            reMap.put("balanceBrokerageAmount",BigDecimal.valueOf(0.00));
//        }else {
//            Map<String,Object>  applyAmountSUMMap= projectBrokerageMapper.getProjectBrokerageInProjectId(projectIds);
//            reMap.put("applyAmountSUM",null == applyAmountSUMMap ? BigDecimal.valueOf(0.00) : (BigDecimal) applyAmountSUMMap.get("applyAmountSUM"));
//            reMap.put("balanceBrokerageAmount",reMap.get("projectBrokerageTotalAmount").subtract(reMap.get("applyAmountSUM")));
//        }
//
//        return success(reMap);
//    }
//
//    @ApiOperation(value = "我参与项目情况统计")
//    @GetMapping("getProjectIdByUserParticipation/{currYear}/{userId}")
//    public  AjaxResult getProjectIdByUserParticipation(@PathVariable("currYear") Integer currYear , @PathVariable("userId") Long userId){
//        HeadStatisticalVO StatisticalAgreement = performanceMapper.StatisticalAgreementParticipation(userId,currYear);
//        Map<String, BigDecimal> reMap = Maps.newHashMap();
//        if (StatisticalAgreement==null){
//            reMap.put("projectBrokerageTotalAmount", BigDecimal.valueOf(0.00));
//        }
//        else {
//            reMap.put("projectBrokerageTotalAmount",StatisticalAgreement.getAgreementAmount().multiply(BigDecimal.valueOf(0.015)));
//        }
//        List<Long> projectIds = Lists.newArrayList();
//        List<Project> projects = performanceMapper.getProjectIdByUserParticipation(currYear,userId);
//        if (projectIds.size()==0){
//            reMap.put("applyAmountSUM",BigDecimal.valueOf(0.00));
//            reMap.put("balanceBrokerageAmount",BigDecimal.valueOf(0.00));
//        }else {
//            for (Project project : projects) {
//                projectIds.add(project.getId());
//            }
//            Map<String,Object>  applyAmountSUMMap= projectBrokerageMapper.getProjectBrokerageInProjectId(projectIds);
//            reMap.put("applyAmountSUM",(null == applyAmountSUMMap&&applyAmountSUMMap.get("applyAmountSUM") == null) ? BigDecimal.valueOf(0.00) : (BigDecimal) applyAmountSUMMap.get("applyAmountSUM"));
//            reMap.put("balanceBrokerageAmount",reMap.get("projectBrokerageTotalAmount").subtract(reMap.get("applyAmountSUM")));
//        }
//
//        return success(reMap);
//    }
//
//}
