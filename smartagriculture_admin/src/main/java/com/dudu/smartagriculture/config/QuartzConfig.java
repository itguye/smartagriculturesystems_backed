package com.dudu.smartagriculture.config;


import com.dudu.smartagriculture.quartz.DongAoJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 定义任务描述和具体的执行时间
 */
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail jobDetail() {
        //指定任务描述具体的实现类
        return JobBuilder.newJob(DongAoJob.class)
                // 指定任务的名称
                .withIdentity("规则系统")
                // 任务描述
                .withDescription("任务描述：用于执行智慧农业的规则引擎任务")
                // 每次任务执行后进行存储
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger trigger() {
        //创建触发器
        return TriggerBuilder.newTrigger()
                // 绑定工作任务
                .forJob(jobDetail())
                // 每隔 5 秒执行一次 job
                .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(5))
                .build();
    }
}
