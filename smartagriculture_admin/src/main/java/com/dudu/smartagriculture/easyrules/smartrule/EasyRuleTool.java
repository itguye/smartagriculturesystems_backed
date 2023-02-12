package com.dudu.smartagriculture.easyrules.smartrule;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.dudu.smartagriculture.dao.RulesDao;
import com.dudu.smartagriculture.dto.RulesFormsDataDto;
import com.dudu.smartagriculture.mbg.mapper.RulesActionsMapper;
import com.dudu.smartagriculture.mbg.mapper.RulesMapper;
import com.dudu.smartagriculture.mbg.model.*;
import com.dudu.smartagriculture.service.DeviceDataService;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;


@Component
public class EasyRuleTool {
    @Resource
    private RulesMapper rulesMapper;
    @Resource
    private RulesDao rulesDao;
    @Resource
    private DeviceDataService deviceDataService;
    @Resource
    private RulesActionsMapper rulesActionsMapper;
    private static final Log logger = LogFactory.getLog(EasyRuleTool.class);
    /**
     * 将前端的数据进行转换成对应的实体数据
     * @param formsDataDto
     * @param prefix
     * @return
     */
    public Rules rulesFromDataToRules(RulesFormsDataDto formsDataDto, String prefix){
        // 规则id,规则名称,规则表达式,操作,备注,表单的id
        Rules rules = new Rules();
        rules.setRuleName(formsDataDto.getSceneName());
        rules.setPrefix(prefix);
        // 规则表达式
        List<RulesTriggers> triggers = formsDataDto.getTriggers();
        String condition = formsDataDto.getConditional();
        StringBuffer buffer = new StringBuffer();
        // 规则表达式
        int count = 1;
        for (RulesTriggers tr : triggers) {
            if (count > 1) {
                buffer.append(" " + condition+" ");
            }
            ++count;
            // trigger.getValue().get(temperature) >= 20
            String key = "\""+tr.getSensorName()+"\"";
            buffer.append(rules.getPrefix() + ".getValue().get(" +key + ")" + tr.getOperator() + tr.getValue());
        }

        //最终生成的表达式条件trigger.getValue().get("temperature")>=39.0 and trigger.getValue().get("humidity")>50.0
        System.out.println("最终生成的表达式条件:" + buffer.toString());
        rules.setRuleExpressions(buffer.toString());
        // 操作
        List<RulesActions> actions = formsDataDto.getActions();
//        // 转换为JSON数据
//        Gson gson = new Gson();
//        String actionsJson = gson.toJson(actions);
//        rules.setOperate(actionsJson);
        StringBuffer operateStr = new StringBuffer();
        for (RulesActions ra : actions) {
            // waterPump:true;
            operateStr.append(ra.getActionName());
            operateStr.append(":");
            operateStr.append(ra.getAct() == true? 1:0);
            operateStr.append(",");
        }
        rules.setOperate(operateStr.toString());

        // 开始时间和结束时间
        rules.setStartTime(formsDataDto.getStartTime());
        rules.setEndTime(formsDataDto.getEndTime());
        // 时间间隔
        rules.setIntervalTime(formsDataDto.getIntervalTime());
        // 备注
        rules.setRemark(formsDataDto.getRemark());
        rules.setFid(formsDataDto.getId());
        return rules;
    }

    /**
     * 执行策略规则
     */
    public void executeRule(){
        // 【1】从数据库中获取规则
        List<Rules> rules = rulesDao.getRules();
        // 【2】初始化参数
        Trigger trigger = new Trigger();
        // 从缓存中获取MQTT服务器的实时数据
        Map<String, Double> currentSensorCache = deviceDataService.getCurrentSensorMapCache();
        if (currentSensorCache == null) {
            logger.info("没有获取到当前数据!");
            return;
        }
        trigger.setValue(currentSensorCache);// 初始化数据
        System.out.println("当前数据情况:"+currentSensorCache.toString());

        // 执行动作
        Action action = new Action();
        Facts facts = new Facts();
        facts.put("trigger", trigger);
        facts.put("action",action);


        // 【3】 规则集合
        List<Rule> ruleList = new ArrayList<>();
        for (Rules rs : rules) {
            Date now = new Date();
                // 更新rule的执行时间
            long time = 60*1000;// 1分钟 60x000 默认
            if (rs.getIntervalTime() != null && rs.getIntervalTime() > 0) {
                time = rs.getIntervalTime();
            }

                if (rs.getExecutionTime() == null){// 表示没有执行过
                    rs.setExecutionTime(new Date(now.getTime()+time));
                }else{
                    // 表示执行过
                    rs.setExecutionTime(new Date(rs.getExecutionTime().getTime()+time));
                }

                rulesMapper.updateByPrimaryKeySelective(rs);
                // 添加该规则
                RulesActionsExample rulesActionsExample = new RulesActionsExample();
                rulesActionsExample.createCriteria().andFidEqualTo(rs.getFid());
                List<RulesActions> rulesActions = rulesActionsMapper.selectByExample(rulesActionsExample);
                StringBuffer endResult = new StringBuffer();
                endResult.append("{");
                int count = rulesActions.size();//3
                // 拼接执行的动作的JSON
                for (RulesActions ra :rulesActions) {
                    endResult.append( "\\\""+ra.getActionName()+"\\\":"+(ra.getAct()==true?1:0));
                    if (count >1) {
                        endResult.append(",");
                    }
                    count --;//2
                }
                endResult.append("}");
                // 创建规则
                Rule alcoholRule = new MVELRule()
                        .name(rs.getRuleName()) //标题
                        .description(rs.getRemark()) // 备注
                        .priority(1)  //有先级
                        .when(rs.getRuleExpressions())
                        // .then("action.send("+"\"{"+rs.getOperate()+"}\""+");");
                        // 当符合上面的条件的时候执行的操作
                        .then("action.send("+"\""+endResult.toString()+"\""+")");
                        //当符合上面的条件的时候执行的操作//"{"fan":1,"plantLed":1,"waterPump":1,}"
                ruleList.add(alcoholRule);
        }

        // 所有的规则创建完成后将规则将入到规则组中
        org.jeasy.rules.api.Rules rulesList = new  org.jeasy.rules.api.Rules();
        ruleList.stream().forEach((rl -> {
            rulesList.register(rl);
        }));

        //创建规则执行引擎，并执行规则
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rulesList, facts);
    }
}
