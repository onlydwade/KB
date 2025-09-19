package com.bytefinger.common.core.web.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class PostUserVO {
    private Long postId;
    private String postName;
    private String realName;
    private String userName;
    private Long userId;
    List<PostUserVO> postUserVOList;

}
