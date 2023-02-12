package com.dudu.smartagriculture.easyrules.smartrule;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRule;

import java.util.*;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        // 模拟缓存中的数据变化
        Map<String, Double> cacheData = new HashMap<>();
        cacheData.put("temperature",1.0);
        cacheData.put("humidity", 20.0);
        double temperature = 10;
        double humidity = 10;
        while(true){
            // 1s获取一次
            Thread.sleep(1000);
            cacheData.put("temperature", temperature++);
            cacheData.put("humidity", humidity++);
            System.out.println("当前温度:"+temperature+"当前湿度:"+humidity);
            List<Rule> ruleList = new ArrayList<>();
            // 前端获取的数据进行处理,最终生成temperature>=20.0
            String rule = ">=";
            Double value = 20.0;
            String sensor = "temperature";

            String rule2 = ">";
            Double value2 = 30.0;
            String sensor2 = "humidity";
            String and = " and ";
            String or = " or ";
            // 从数据库中提取,条件
//            String expression = sensor+rule+value;
            Map<String, String> expressionMap = new HashMap<>();
            expressionMap.put(sensor, rule + value);
            expressionMap.put(sensor2, rule2 + value2);
            // 从数据库提取,策略信息
            String ruleName = "策略1";
            String description = "当温度大于等于20时，打开水泵";
            // 生成表达式
//            String  endRule = "trigger."+expression;
            String endRule="";
            Set<String> strings = expressionMap.keySet();
            // 获取所有的key
            int count = 0;
            for (String key : strings) {
                ++count;
                if (count >= 2) {
                    endRule +=and;
                }
                // 对key进行加工
                String keyStr = "\""+key+"\"";
                endRule= endRule+("trigger.getValue().get("+keyStr+")"+expressionMap.get(key));//trigger.getValue().get(temperature) >= 20
            }

            System.out.println("endRule:"+endRule);
            // 从数据库中提取，需要执行的动作
            String msg = "{waterPump:true}";

            // 开始生成策略
            // 获取现有条件
            Trigger trigger = new Trigger();
            Map<String, Double> values = new HashMap<>();
            // 获取所有的key
            for (String key : strings) {
                if (cacheData.containsKey(key)) {
                    // 如果数据库中存在该值进行初始化操作
                    values.put(key, cacheData.get(key));
                }
            }
            trigger.setValue(values);


            // 执行动作
            Action action = new Action();
            action.setSendMsg(msg);// 从数据库中获取

            Facts facts = new Facts();
            facts.put("trigger", trigger);
            facts.put("action",action);

            // 创建规则1
            Rule alcoholRule = new MVELRule()
                    .name(ruleName) //标题
                    .description(description) // 备注
                    .priority(1)  //有先级
                    .when(endRule)  //可以理解为条件 ，支持多个 比如 (parson.age == 18) || (parson.name == "Tom")  所有支持的表达式 在这个类下org.mvel2.debug.DebugTools
                    .then("action.send(action.sendMsg);"); //当符合上面的条件的时候执行的操作


            ruleList.add(alcoholRule);
            // 所有的规则创建完成后将规则将入到规则组中
            Rules rules = new Rules();
            ruleList.stream().forEach((rule1 -> {
                rules.register(rule1);
            }));

            //创建规则执行引擎，并执行规则
            RulesEngine rulesEngine = new DefaultRulesEngine();
            rulesEngine.fire(rules, facts);
        }

    }
}
