package com.dudu.smartagriculture.service.impl;

import com.dudu.smartagriculture.common.exception.Asserts;
import com.dudu.smartagriculture.dao.RulesActionsDao;
import com.dudu.smartagriculture.dao.RulesDao;
import com.dudu.smartagriculture.dao.RulesTriggersDao;
import com.dudu.smartagriculture.dto.RulesFormsDataDto;
import com.dudu.smartagriculture.dto.RulesListDto;
import com.dudu.smartagriculture.easyrules.smartrule.EasyRuleTool;
import com.dudu.smartagriculture.mbg.mapper.RulesActionsMapper;
import com.dudu.smartagriculture.mbg.mapper.RulesFormsMapper;
import com.dudu.smartagriculture.mbg.mapper.RulesMapper;
import com.dudu.smartagriculture.mbg.mapper.RulesTriggersMapper;
import com.dudu.smartagriculture.mbg.model.*;
import com.dudu.smartagriculture.service.DeviceRulesService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DeviceRulesServiceImpl implements DeviceRulesService {
    @Resource
    private RulesActionsDao actionsDao;
    @Resource
    private RulesTriggersDao triggersDao;
    @Resource
    private RulesDao rulesDao;

    @Resource
    private EasyRuleTool easyRuleTool;
    @Resource
    private RulesFormsMapper rulesFormsMapper;
    @Resource
    private RulesMapper rulesMapper;
    @Resource
    private RulesActionsMapper rulesActionsMapper;
    @Resource
    private RulesTriggersMapper rulesTriggersMapper;




    @Transactional
    @Override
    public void addRules(RulesFormsDataDto forms, String prefix) {
        // 判断当前策略是否存在
        RulesFormsExample rulesFormsExample = new RulesFormsExample();
        rulesFormsExample.createCriteria().andSceneNameEqualTo(forms.getSceneName());
        List<RulesForms> resultForms = rulesFormsMapper.selectByExample(rulesFormsExample);
        if (resultForms.size() > 0) {
            // 表示存在该策略
            Asserts.fail("该策略名已经存在，不可重复添加相同策略名");
            return;
        }

        // 保存表单信息
        RulesForms rulesForms = new RulesForms();
        rulesForms.setConditional(forms.getConditional());
        rulesForms.setRemark(forms.getRemark());
        rulesForms.setSceneName(forms.getSceneName());
        rulesForms.setCreateTime(forms.getCreateTime() !=null ? forms.getCreateTime():new Date());
        rulesForms.setUpdateTime(forms.getUpdateTime() !=null ?forms.getUpdateTime():new Date());
        rulesForms.setStartTime(forms.getStartTime());
        rulesForms.setEndTime(forms.getEndTime());
        rulesForms.setIntervalTime(forms.getIntervalTime());
        rulesFormsMapper.insert(rulesForms);

        // 保存rules_actions和rules_triggers
        List<RulesActions> actions = forms.getActions();
        actions.stream().forEach((at)->{
            at.setFid(rulesForms.getId());
        });
        List<RulesTriggers> triggers = forms.getTriggers();
        triggers.stream().forEach((tr)->{
            tr.setFid(rulesForms.getId());
        });
        actionsDao.addActionsList(actions);
        triggersDao.addTriggersList(triggers);

        // 保存规则信息
        Rules rules = easyRuleTool.rulesFromDataToRules(forms, prefix);// 生成规则对象
        rules.setFid(rulesForms.getId());
        rules.setCreateTime(new Date());
        rules.setUpdateTime(new Date());
        rulesMapper.insert(rules);
    }

    @Override
    public List<RulesListDto> getAllListByPagination(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<RulesListDto> allList = getAllListByKeyword(keyword);
        return allList;
    }
    @Override
    public List<RulesListDto> getAllListByKeyword(String keyword) {
        if (keyword != null) {
            keyword = keyword.replaceAll("_", "\\\\_");
            keyword = keyword.replaceAll("%", "\\\\%");
        }

        return rulesDao.getArrList(keyword);
    }



    @Transactional(rollbackFor=Exception.class)
    @Override
    public void delRules(List<Integer> ids) {

        rulesDao.setForeignKeyChecks(0);// 关闭外键约束
        RulesFormsExample rulesFormsExample = new RulesFormsExample();
        rulesFormsExample.createCriteria().andIdIn(ids);
        rulesFormsMapper.deleteByExample(rulesFormsExample);// 删除forms表

        RulesActionsExample rulesActionsExample = new RulesActionsExample();
        rulesActionsExample.createCriteria().andFidIn(ids);
        rulesActionsMapper.deleteByExample(rulesActionsExample);// 删除actions表


        RulesTriggersExample rulesTriggersExample = new RulesTriggersExample();
        rulesTriggersExample.createCriteria().andFidIn(ids);
        rulesTriggersMapper.deleteByExample(rulesTriggersExample);// 删除trigger表

        RulesExample rulesExample = new RulesExample();
        rulesExample.createCriteria().andFidIn(ids);
        rulesMapper.deleteByExample(rulesExample);// 删除rules表
        rulesDao.setForeignKeyChecks(1);// 开启外键约束

    }

    @Override
    public RulesFormsDataDto getSceneById(Integer id) {
        RulesFormsDataDto rulesFormsDataDto = new RulesFormsDataDto();

        // 获取执行器
        RulesActionsExample rulesActionsExample = new RulesActionsExample();
        rulesActionsExample.createCriteria().andFidEqualTo(id);
        List<RulesActions> rulesActions = rulesActionsMapper.selectByExample(rulesActionsExample);
        rulesFormsDataDto.setActions(rulesActions);


        // 获取条件
        RulesTriggersExample rulesTriggersExample = new RulesTriggersExample();
        rulesTriggersExample.createCriteria().andFidEqualTo(id);
        List<RulesTriggers> rulesTriggers = rulesTriggersMapper.selectByExample(rulesTriggersExample);
        rulesFormsDataDto.setTriggers(rulesTriggers);

        // 获取表单数据
        RulesForms rulesForms = rulesFormsMapper.selectByPrimaryKey(id);
        rulesFormsDataDto.setConditional(rulesForms.getConditional());
        rulesFormsDataDto.setSceneName(rulesForms.getSceneName());
        rulesFormsDataDto.setRemark(rulesForms.getRemark());
        rulesFormsDataDto.setCreateTime(rulesForms.getCreateTime());
        rulesFormsDataDto.setId(id);
        rulesFormsDataDto.setStartTime(rulesForms.getStartTime());
        rulesFormsDataDto.setEndTime(rulesForms.getEndTime());
        rulesFormsDataDto.setIntervalTime(rulesForms.getIntervalTime());
        return rulesFormsDataDto;
    }

    @Transactional
    @Override
    public void updateScene(RulesFormsDataDto forms,String prefix) {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(forms.getId());
        delRules(integers);// 删除规则
        addRules(forms,prefix);// 添加规则
    }

}
