package com.dudu.smartagriculture.mbg.mapper;

import com.dudu.smartagriculture.mbg.model.RulesActions;
import com.dudu.smartagriculture.mbg.model.RulesActionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RulesActionsMapper {
    long countByExample(RulesActionsExample example);

    int deleteByExample(RulesActionsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RulesActions row);

    int insertSelective(RulesActions row);

    List<RulesActions> selectByExample(RulesActionsExample example);

    RulesActions selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") RulesActions row, @Param("example") RulesActionsExample example);

    int updateByExample(@Param("row") RulesActions row, @Param("example") RulesActionsExample example);

    int updateByPrimaryKeySelective(RulesActions row);

    int updateByPrimaryKey(RulesActions row);
}