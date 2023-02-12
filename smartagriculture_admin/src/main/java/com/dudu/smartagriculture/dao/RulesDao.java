package com.dudu.smartagriculture.dao;

import com.dudu.smartagriculture.dto.RulesListDto;
import com.dudu.smartagriculture.mbg.model.Rules;

import java.util.HashMap;
import java.util.List;

public interface RulesDao {
    List<RulesListDto> getArrList(String keyword);
    int setForeignKeyChecks(Integer id);
    List<Rules> getRules();
}
