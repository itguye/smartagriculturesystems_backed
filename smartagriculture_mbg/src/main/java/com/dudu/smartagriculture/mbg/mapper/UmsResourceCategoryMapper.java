package com.dudu.smartagriculture.mbg.mapper;

import com.dudu.smartagriculture.mbg.model.UmsResourceCategory;
import com.dudu.smartagriculture.mbg.model.UmsResourceCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UmsResourceCategoryMapper {
    long countByExample(UmsResourceCategoryExample example);

    int deleteByExample(UmsResourceCategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsResourceCategory row);

    int insertSelective(UmsResourceCategory row);

    List<UmsResourceCategory> selectByExample(UmsResourceCategoryExample example);

    UmsResourceCategory selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") UmsResourceCategory row, @Param("example") UmsResourceCategoryExample example);

    int updateByExample(@Param("row") UmsResourceCategory row, @Param("example") UmsResourceCategoryExample example);

    int updateByPrimaryKeySelective(UmsResourceCategory row);

    int updateByPrimaryKey(UmsResourceCategory row);
}