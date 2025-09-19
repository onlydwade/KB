package com.bytefinger.common.core.web.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 岗位VO
 *
 * @author pat
 * @date 2022/11/04 10:43
 **/
@Data
public class PostVO {

    private Long postId;

    private String postName;

    private List<UserVO> userVOList;

}
