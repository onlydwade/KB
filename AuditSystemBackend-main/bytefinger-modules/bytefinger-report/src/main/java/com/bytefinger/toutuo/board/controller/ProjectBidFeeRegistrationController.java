package com.bytefinger.toutuo.board.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.utils.ServletUtils;
import com.bytefinger.common.core.web.controller.BaseController;
import com.bytefinger.common.core.web.domain.vo.AjaxResult;
import com.bytefinger.common.das.domain.vo.QueryPage;
import com.bytefinger.common.security.annotation.DataFill;
import com.bytefinger.common.security.utils.poi.ExcelUtil;
import com.bytefinger.toutuo.board.aop.GetDataRole;
import com.bytefinger.toutuo.board.domain.ProjectBidFeeRegistration;
import com.bytefinger.toutuo.board.mapper.ProjectBidFeeRegistrationMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 投标费用台帐 前端控制器
 * </p>
 *
 * @author lc
 * @since 2023-03-14
 */
@Slf4j
@Api(tags = "投标费用台帐")
@AllArgsConstructor
@RestController
@RequestMapping("/projectBidFeeRegistration")
public class ProjectBidFeeRegistrationController extends BaseController {
    private final ProjectBidFeeRegistrationMapper projectBidFeeRegistrationMapper;

    @ApiOperation(value = "分页（带条件）-项目投标费用台帐")
    @PostMapping("/page")
    public AjaxResult page(@RequestBody QueryPage queryPage) {
        return success(getBfrParams(queryPage));
    }
    @ApiOperation(value = "导出-项目投标费用台帐")
    @DataFill
    @PostMapping("/export")
    public void export(@RequestBody QueryPage queryPage) {
        queryPage.setPageSize(Integer.MAX_VALUE);
        ExcelUtil<ProjectBidFeeRegistration> excelUtil = new ExcelUtil<>(ProjectBidFeeRegistration.class);
        List<ProjectBidFeeRegistration> projectBidFeeRegistrations = getBfrParams(queryPage).getRecords();
        for (ProjectBidFeeRegistration projectBidFeeRegistration: projectBidFeeRegistrations) {
            if (projectBidFeeRegistration.getFreeType().equals("TOU_BIAO_FEI_YONG_QI_TA")){
                projectBidFeeRegistration.setFreeType(projectBidFeeRegistration.getFreeTypeOther());
            }
        }
        excelUtil.exportExcel(ServletUtils.getResponse(), projectBidFeeRegistrations, "项目台帐数据");
    }

    private Page<ProjectBidFeeRegistration> getBfrParams(@RequestBody QueryPage queryPage) {
        Map<String, Object> bfrParams = new HashMap<>();
        if (queryPage.getParams().get("freeType")!=null){
            bfrParams.put("freeType",queryPage.getParams().get("freeType"));
            queryPage.getParams().remove("freeType");
        }
        if (queryPage.getParams().get("recoverStatus")!=null){
            bfrParams.put("recoverStatus",queryPage.getParams().get("recoverStatus"));
            queryPage.getParams().remove("recoverStatus");
        }
        if (queryPage.getParams().get("deduct")!=null){
            bfrParams.put("deduct",queryPage.getParams().get("deduct"));
            queryPage.getParams().remove("deduct");
        }
        if (queryPage.getLikeParams().get("paymentCompany")!=null){
            bfrParams.put("paymentCompany",queryPage.getLikeParams().get("paymentCompany"));
            queryPage.getLikeParams().remove("paymentCompany");
        }
        if (queryPage.getLikeParams().get("arrearageCompany")!=null){
            bfrParams.put("arrearageCompany",queryPage.getLikeParams().get("arrearageCompany"));
            queryPage.getLikeParams().remove("arrearageCompany");
        }
        if (queryPage.getLikeParams().get("payeeCompany")!=null){
            bfrParams.put("payeeCompany",queryPage.getLikeParams().get("payeeCompany"));
            queryPage.getLikeParams().remove("payeeCompany");
        }
        Page<ProjectBidFeeRegistration> pages = projectBidFeeRegistrationMapper.page(queryPage.toPage(), queryPage.getWrapperByPrefix("project."),new GetDataRole().dataRole(),bfrParams);
        return pages ;
    }
}
