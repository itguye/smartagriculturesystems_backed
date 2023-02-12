package com.dudu.smartagriculture.service;

import com.dudu.smartagriculture.dto.RulesFormsDataDto;
import com.dudu.smartagriculture.dto.RulesListDto;

import java.util.List;

public interface DeviceRulesService {
    public void addRules(RulesFormsDataDto forms, String prefix);
    public List<RulesListDto> getAllListByPagination(String keyword, Integer pageSize, Integer pageNum);
    public List<RulesListDto> getAllListByKeyword(String keyword);
    public void delRules(List<Integer> ids);
    public RulesFormsDataDto getSceneById(Integer id);
    public void updateScene(RulesFormsDataDto forms,String prefix);
}
