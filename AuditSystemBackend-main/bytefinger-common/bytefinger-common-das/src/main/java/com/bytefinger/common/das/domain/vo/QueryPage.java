package com.bytefinger.common.das.domain.vo;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bytefinger.common.core.utils.CamelUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询分页
 *
 * @author pat
 * @date 2022/10/06 11:45
 **/
@Data
@Accessors(chain = true)
public class QueryPage {

    @ApiModelProperty("当前页")
    private Integer pageNo = 1;

    @ApiModelProperty("每页显示数")
    private Integer pageSize = 10;

    @ApiModelProperty("搜索类型 1:精确搜索, 2:模糊搜索")
    private Integer searchType = 2;

    @ApiModelProperty("搜索框中内容")
    private String content = "";

    @ApiModelProperty("年")
    private Integer year ;

    @ApiModelProperty(name = "需要检索的字段", dataType = "List", notes = "'title'")
    private String contentColumn;

    @ApiModelProperty(name = "需要检索的字段", dataType = "List", notes = "'title,content'")
    private List<String> contentColumns;

    @ApiModelProperty(name = "对等条件，不支持in", dataType = "Map", notes = "{'id':1,'name':xxx}")
    private Map<String, Object> params = new HashMap<>();

    @ApiModelProperty(name = "and like条件", dataType = "Map", notes = "{'id':1,'name':xxx}")
    private Map<String, Object> likeParams = new HashMap<>();

    @ApiModelProperty(name = "> 大于")
    private Map<String, Object> gtParams = new HashMap<>();

    @ApiModelProperty(name = "< 小于")
    private Map<String, Object> ltParams = new HashMap<>();

    @ApiModelProperty(name = ">= 大于等于")
    private Map<String, Object> geParams = new HashMap<>();

    @ApiModelProperty(name = "<= 小于等于")
    private Map<String, Object> leParams = new HashMap<>();

    @ApiModelProperty(name = "in 多条件", dataType = "Map", notes = "{'state':[1,2,3]}")
    private Map<String, List<Object>> inParams = new HashMap<>();

    @ApiModelProperty(name = "not in 多条件", dataType = "Map", notes = "{'state':[1,2,3]}")
    private Map<String, List<Object>> notInParams = new HashMap<>();

    @ApiModelProperty(name = "升序字段", dataType = "Map", notes = "['a1', 'b2']")
    private List<String> asc = new ArrayList<>();

    @ApiModelProperty(name = "降序字段", dataType = "Map", notes = "['a1', 'b2']")
    private List<String> desc = new ArrayList<>();

    @ApiModelProperty(hidden = true)
    private Map<String, Object> dbParams = new HashMap<>();

    public <T> Page<T> toPage() {
        Page<T> page = new Page<>(pageNo, pageSize);
        if (!asc.isEmpty()) {
            for (String column : asc) {
                page.addOrder(OrderItem.asc(CamelUtils.camelToUndeline(column)));
            }
        }

        if (!desc.isEmpty()) {
            for (String column : desc) {
                page.addOrder(OrderItem.desc(CamelUtils.camelToUndeline(column)));
            }
        }

        return page;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public <T> QueryWrapper<T> getWrapperByPrefix(String prefix) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(content) && StringUtils.isNotBlank(contentColumn)) {
            wrapper.like(prefix + CamelUtils.camelToUndeline(contentColumn), content);
        }

        if (StringUtils.isNotBlank(content) && CollUtil.isNotEmpty(contentColumns)) {
            wrapper.and(item -> {
                for (String column : contentColumns) {
                    item.or().like(prefix + CamelUtils.camelToUndeline(column), content);
                }
            });
        }

        if (!params.isEmpty()) {
            params.forEach((s, o) -> wrapper.eq(prefix + CamelUtils.camelToUndeline(s), o));
        }

        if (!inParams.isEmpty()) {
            inParams.forEach((s, o) -> wrapper.in(prefix + CamelUtils.camelToUndeline(s), o));
        }

        if (!notInParams.isEmpty()) {
            notInParams.forEach((s, o) -> wrapper.notIn(prefix + CamelUtils.camelToUndeline(s), o));
        }

        if (!gtParams.isEmpty()) {
            gtParams.forEach((s, o) -> wrapper.gt(prefix + CamelUtils.camelToUndeline(s), o));
        }

        if (!ltParams.isEmpty()) {
            ltParams.forEach((s, o) -> wrapper.lt(prefix + CamelUtils.camelToUndeline(s), o));
        }

        if (!geParams.isEmpty()) {
            geParams.forEach((s, o) -> wrapper.ge(prefix + CamelUtils.camelToUndeline(s), o));
        }

        if (!leParams.isEmpty()) {
            leParams.forEach((s, o) -> wrapper.le(prefix + CamelUtils.camelToUndeline(s), o));
        }

        if (!likeParams.isEmpty()) {
            likeParams.forEach((s, o) -> wrapper.like(prefix + CamelUtils.camelToUndeline(s), o));
        }

        if (wrapper.isEmptyOfWhere()) {
            wrapper.apply(" 1 = 1 ");
        }

        return wrapper;
    }

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public <T> QueryWrapper<T> getWrapper() {
        return this.<T>getWrapperByPrefix("");
    }

}
