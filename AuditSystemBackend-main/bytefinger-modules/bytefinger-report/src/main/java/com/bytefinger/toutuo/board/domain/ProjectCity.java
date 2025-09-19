package com.bytefinger.toutuo.board.domain;

import lombok.Data;

@Data
public class ProjectCity {

    private   Integer projectCount = 0;

    private   Long areaCode = 0L;

    private   String areaName ;

    private   String parentName ;

    private   Long provinceCode = 0L;
}
