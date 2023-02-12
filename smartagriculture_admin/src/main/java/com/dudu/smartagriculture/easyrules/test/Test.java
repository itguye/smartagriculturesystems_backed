package com.dudu.smartagriculture.easyrules.test;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.mvel.MVELRule;

public class Test {
    /**
     * 下面的案例 简单的说就是将一堆的if eles 简化了 使用
     *  easy rules 引擎计算了一切，方便很多
     * @param args
     */
    public static void main(String[] args) {

        //创建一个Person实例(Fact)
        Person tom = new Person("Tom", 20);
        Facts facts = new Facts();
        facts.put("person", tom);
        System.out.println("执行规则前"+tom);
        //链式编程
        Rule ageRule = new RuleBuilder()
                .name("age rule")
                .description("Check if person's age is > 18 and marks the person as adult")
                .priority(1)
                .when(f -> {
                    Person person = f.get("person");
                    return person.getAge() > 18;
                })
                .then(f -> {
                    Person person = f.get("person");
                    person.setAdult(true);
                })
                .build();
        //表达式 -- 常用的 不支持正则匹配
        Rule alcoholRule = new MVELRule()
                .name("age rule") //标题
                .description("Check if person's age is > 18 and marks the person as adult") // 备注
                .priority(1)  //有先级
                .when("person.name contains \"m\"")  //可以理解为条件 ，支持多个 比如 (parson.age == 18) || (parson.name == "Tom")  所有支持的表达式 在这个类下org.mvel2.debug.DebugTools
                .then("person.setAdult(true);"); //当符合上面的条件的时候执行的操作

//        //脚步形式 -- 支持使用正则匹配
//        Rule spELRule = new SpELRule()
//                .name("age rule")
//                .description("Check if person's age is > 18 and marks the person as adult")
//                .priority(1)
//                .when("#{['person'].age > 2}") //org.springframework.expression.spel.standard.TokenKind 支持的全部表达式
//                .then("#{['person'].setAdult(true)}");

        //还可以支持yaml的形式
        //。。。。。。yaml 和上面的支持格式是一样的，写的地方不一致而已，项目中使用表达式的方式居多
//
        Rules rules = new Rules();
//        rules.register(spELRule);
        rules.register(alcoholRule);
        rules.register(ageRule);
//        rules.register(ageRule);

        //创建规则执行引擎，并执行规则
        RulesEngine rulesEngine = new DefaultRulesEngine();
        rulesEngine.fire(rules, facts);
        System.out.println("执行规则后"+tom);
    }

}
