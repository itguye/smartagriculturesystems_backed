package com.dudu.smartagriculture.mbg.mapper;

import com.dudu.smartagriculture.mbg.model.RulesTriggers;
import com.dudu.smartagriculture.mbg.model.RulesTriggersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RulesTriggersMapper {
    long countByExample(RulesTriggersExample example);

    int deleteByExample(RulesTriggersExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(RulesTriggers row);

    int insertSelective(RulesTriggers row);

    List<RulesTriggers> selectByExample(RulesTriggersExample example);

    RulesTriggers selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("row") RulesTriggers row, @Param("example") RulesTriggersExample example);

    int updateByExample(@Param("row") RulesTriggers row, @Param("example") RulesTriggersExample example);

    int updateByPrimaryKeySelective(RulesTriggers row);

    int updateByPrimaryKey(RulesTriggers row);
}