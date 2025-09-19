package com.bytefinger.toutuo.biz.project.dto;

import com.bytefinger.toutuo.biz.project.domain.ProjectEstimatedCost;
import com.bytefinger.toutuo.biz.project.domain.ProjectExpand;
import lombok.Data;

import java.util.List;

@Data
public class SaveClosureInfoDto {

    public Long projectId;

    public ProjectExpand projectExpand;

    public List<ProjectEstimatedCost> projectEstimatedCosts;
}
