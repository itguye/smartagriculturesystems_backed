package com.dudu.smartagriculture.mbg.mapper;

import com.dudu.smartagriculture.mbg.model.Rules;
import com.dudu.smartagriculture.mbg.model.RulesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RulesMapper {
    long countByExample(RulesExample example);

    int deleteByExample(RulesExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Rules row);

    int insertSelective(Rules row);

    List<Rules> selectByExample(RulesExample example);

    Rules selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") Rules row, @Param("example") RulesExample example);

    int updateByExample(@Param("row") Rules row, @Param("example") RulesExample example);

    int updateByPrimaryKeySelective(Rules row);

    int updateByPrimaryKey(Rules row);
}