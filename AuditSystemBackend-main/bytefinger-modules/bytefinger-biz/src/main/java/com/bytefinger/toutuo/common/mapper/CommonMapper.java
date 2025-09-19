package com.bytefinger.toutuo.common.mapper;

import com.bytefinger.common.core.web.domain.vo.CommonNameVO;
import com.bytefinger.toutuo.common.domain.BizUserBaseEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通用查询
 *
 * @author pat
 * @date 2022/11/04 15:16
 **/
public interface CommonMapper {


    /**
     * 通用的名字查询
     *
     * @param tableName
     * @param columnName
     * @param ids
     * @return
     */
    List<CommonNameVO> listCommonName(@Param("tableName") String tableName, @Param("columnName") String columnName, @Param("ids") List<Long> ids);

    /**
     * 获取业务数据
     *
     * @param sql
     * @return
     */
    List<Map<String, Object>> listBizData(@Param("sql") String sql);

    /**
     * 修改业务数据
     *
     * @param sql
     * @return
     */
    int updateBiz(@Param("sql") String sql);

    /**
     * 通用的参与人表查询
     *
     * @param tableName
     * @param ids
     * @return
     */
    List<BizUserBaseEntity> listBizUser(@Param("tableName") String tableName, @Param("ids") List<Long> ids);

}
