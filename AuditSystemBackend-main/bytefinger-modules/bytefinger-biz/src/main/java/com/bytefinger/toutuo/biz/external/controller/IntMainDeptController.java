package com.bytefinger.toutuo.biz.external.controller;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.bytefinger.common.core.utils.StringUtils;
import com.bytefinger.common.core.utils.TreeUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.toutuo.api.biz.user.domain.IntMainBm;
import com.bytefinger.toutuo.api.biz.user.domain.IntMainDept;
import com.bytefinger.toutuo.biz.external.service.IIntMainBmService;
import com.bytefinger.toutuo.biz.external.service.IIntMainDeptService;
import com.bytefinger.toutuo.biz.user.mapper.SysDeptMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.ast.NullLiteral;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 主数据组织表 前端控制器
 * </p>
 *
 * @author Jone
 * @since 2023-02-13
 */
@Slf4j
@Api(tags = "主数据组织表")
@AllArgsConstructor
@RestController
@RequestMapping("/mainDept")
public class IntMainDeptController extends BaseController {
    private final IIntMainDeptService intMainDeptService;
    private final IIntMainBmService intMainBmService;
    private final SysDeptMapper deptMapper;


    @ApiOperation(value = "详情-主数据组织表")
    @GetMapping("/get/{id}")
    public AjaxResult get(@PathVariable("id") Integer id) {
        return success(intMainDeptService.getById(id));
    }

    @ApiOperation(value = "列表-主数据组织表")
    @GetMapping("/list")
    public AjaxResult list() {
        List<IntMainDept> list = intMainDeptService.list();
        return success(list);
    }

    @ApiOperation(value = "树-主数据组织表")
    @GetMapping("/tree")
    public AjaxResult tree() {
        List<IntMainDept> deptList = intMainDeptService.list();
        List<IntMainBm> bmList = intMainBmService.list();

        List<IntMainDept> deptBms = new ArrayList<>();
        bmList.stream().forEach(v -> {
                IntMainDept dept = new IntMainDept();
                dept.setDesc1(v.getCode());
                dept.setDesc2(v.getDesc2());
                dept.setDesc3(v.getDesc4());
                dept.setDesc4(v.getDesc15());
                dept.setDesc5(v.getDesc16());
                dept.setDept(v.getDesc6());
                dept.setOptional(true);
                deptBms.add(dept);
        });

        List<IntMainDept> bmTree = recursionBm(deptBms, "");

        List<IntMainDept> tree = recursion(deptList, "J1000", bmTree);
        return success(tree);
    }

    @ApiOperation(value = "参标单位列表")
    @GetMapping("/getCBOrgList")
    public AjaxResult getCBOrgList() {
        List<String> list = deptMapper.getCBOrgList();
        return success(list);
    }

    @ApiOperation(value = "人力组织树-主数据组织表")
    @GetMapping("/hrOrg/tree")
    public AjaxResult hrOrgTree() {
        List<IntMainDept> deptList = intMainDeptService.list();
        List<IntMainDept> tree = recursionHR(deptList, "J1000", null);
        return success(tree);
    }

    private List<IntMainDept> recursionHR(List<IntMainDept> depts, String parentId, List<IntMainDept> bmList){
        List<IntMainDept> deptList = new ArrayList<>();
        List<IntMainDept> entitys = depts.stream().filter(v -> v.getDesc4().equals(parentId)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(entitys) || entitys.size() == 0) return deptList;
        for(IntMainDept entity : entitys){
            List<IntMainDept> recursion = recursionHR(depts, entity.getCode(), bmList);
            entity.setChilds(recursion);
            deptList.add(entity);
        }
        return deptList;
    }

    private List<IntMainDept> recursion(List<IntMainDept> depts, String parentId, List<IntMainDept> bmList){
        List<IntMainDept> deptList = new ArrayList<>();
        List<IntMainDept> entitys = depts.stream().filter(v -> v.getDesc4().equals(parentId)).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(entitys) || entitys.size() == 0) return deptList;
        for(IntMainDept entity : entitys){
            List<IntMainDept> recursion = recursion(depts, entity.getDesc1(), bmList);
            bmList.stream().forEach(v -> {
                if(StringUtils.equals(v.getDept(), entity.getDesc1()) && (null == v.getDesc5() || v.getDesc5().equals(""))){
                    recursion.add(v);
                }
            });
            entity.setChilds(recursion);
            deptList.add(entity);
        }
        return deptList;
    }

    private List<IntMainDept> recursionBm(List<IntMainDept> depts, String parentId){
        List<IntMainDept> deptList = new ArrayList<>();
        List<IntMainDept> entitys = depts.stream().filter(v -> parentId.equals(v.getDesc5())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(entitys) || entitys.size() == 0) return deptList;
        for(IntMainDept entity : entitys){
            List<IntMainDept> recursion = recursionBm(depts, entity.getDesc4());
            entity.setChilds(recursion);
            deptList.add(entity);
        }
        return deptList;
    }

    @ApiOperation(value = "分页（带条件）-主数据组织表")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(intMainDeptService.page(queryPage.toPage(), queryPage.getWrapper()));
    }

    @ApiOperation("根据人力组织id获取参标单位")
    @GetMapping(value = "/hrOrg/{orgName}")
    public AjaxResult getHrOrgIdByOrgName(@PathVariable("orgName") String orgName){

        return  AjaxResult.success(deptMapper.getHrOrgIdByOrgName(orgName));
    }
}
