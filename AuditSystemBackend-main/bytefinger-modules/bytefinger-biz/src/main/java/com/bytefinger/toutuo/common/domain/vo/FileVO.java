package com.bytefinger.toutuo.common.domain.vo;

import lombok.Data;

/**
 * @author pat
 * @date 2022/11/28 19:00
 **/
@Data
public class FileVO {

    private String name;
    private String ext;

    private Long stepMenuId;

    private Long documentTemplateId;

}
