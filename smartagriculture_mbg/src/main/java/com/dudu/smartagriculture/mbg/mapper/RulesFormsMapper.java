package com.dudu.smartagriculture.mbg.mapper;

import com.dudu.smartagriculture.mbg.model.RulesForms;
import com.dudu.smartagriculture.mbg.model.RulesFormsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RulesFormsMapper {
    long countByExample(RulesFormsExample example);

    int deleteByExample(RulesFormsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RulesForms row);

    int insertSelective(RulesForms row);

    List<RulesForms> selectByExample(RulesFormsExample example);

    RulesForms selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") RulesForms row, @Param("example") RulesFormsExample example);

    int updateByExample(@Param("row") RulesForms row, @Param("example") RulesFormsExample example);

    int updateByPrimaryKeySelective(RulesForms row);

    int updateByPrimaryKey(RulesForms row);
}